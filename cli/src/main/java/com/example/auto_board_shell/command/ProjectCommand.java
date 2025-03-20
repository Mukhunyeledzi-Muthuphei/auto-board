package com.example.auto_board_shell.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import com.example.auto_board_shell.service.RequestService;
import com.example.auto_board_shell.service.FormatterService;
import com.example.auto_board_shell.service.APIResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.auto_board_shell.helpers.CurrentUser;

@ShellComponent
public class ProjectCommand {

    private final RequestService requestService;
    private final FormatterService formatterService;

    @Autowired
    public ProjectCommand(RequestService requestService, FormatterService formatterService) {
        this.requestService = requestService;
        this.formatterService = formatterService;
    }

    // project-create --name "new pro" --status "2" --desc "from cli" --ownerId
    // "google_user_101"
    @ShellMethod(key = "project-create", value = "Create a new project.")
    public void createProject() {
        try {
            formatterService.printInfo("Creating new project...");

            String name = formatterService.prompt("Enter project name: ");
            String description = formatterService.prompt("Enter project description: ");

            Map<String, Object> owner = new HashMap<>();
            owner.put("id", CurrentUser.getId());

            Map<String, Object> status = new HashMap<>();
            status.put("id", "1");

            Map<String, Object> project = new HashMap<>();
            project.put("name", name);
            project.put("description", description);
            project.put("status", status);
            project.put("owner", owner);

            APIResponse<Map<String, Object>> response = requestService.post(
                    "/projects",
                    project,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            if (response.getData() == null) {
                formatterService.printError("Failed to create project: " + response.getMessage());
                return;
            }

            Object data = response.getData();
            if (data instanceof Map) {
                Map<String, Object> projectResult = (Map<String, Object>) data;
                formatterService.printInfo("ID: " + projectResult.get("id"));
                formatterService.printInfo("Name: " + projectResult.get("name"));
            } else {
                formatterService.printError("Unexpected response type: " + data.getClass().getName());
            }

        } catch (Exception e) {
            formatterService.printError("Error creating project: " + e.getMessage());
        }
    }

    @ShellMethod(key = "project-update", value = "Update a project.")
    public void updateProject(
            @ShellOption(value = "--id", help = "Project ID") String projectId,
            @ShellOption(value = "--name", help = "Project name", defaultValue = ShellOption.NULL) String name,
            @ShellOption(value = "--description", help = "Project description", defaultValue = ShellOption.NULL) String description,
            @ShellOption(value = "--statusId", help = "Status ID", defaultValue = ShellOption.NULL) String statusId) {
        try {
            formatterService.printInfo("Updating project...");

            // Prepare the project update payload
            Map<String, Object> project = new HashMap<>();

            // Add only the provided fields to the payload
            if (name != null) {
                project.put("name", name);
            }
            if (description != null) {
                project.put("description", description);
            }
            if (statusId != null) {
                Map<String, Object> status = new HashMap<>();
                status.put("id", statusId);
                project.put("status", status);
            }

            // If no fields are provided, prompt the user for all attributes
            if (project.isEmpty()) {
                APIResponse<List<Map<String, Object>>> response = requestService.get(
                        "/project-status",
                        new ParameterizedTypeReference<List<Map<String, Object>>>() {
                        });

                List<Map<String, Object>> statuses = response.getData();

                if (statuses == null || statuses.isEmpty()) {
                    formatterService.printError("No statuses available.");
                    return;
                }

                List<String> headers = new ArrayList<>(statuses.get(0).keySet());
                List<List<String>> data = statuses.stream()
                        .map(status -> headers.stream()
                                .map(key -> String.valueOf(status.getOrDefault(key, "N/A")))
                                .toList())
                        .toList();
                formatterService.printTable(headers, data);

                name = formatterService.prompt("Enter project name: ");
                description = formatterService.prompt("Enter project description: ");
                statusId = formatterService.prompt("Enter status id: ");

                Map<String, Object> status = new HashMap<>();
                status.put("id", statusId);

                project.put("name", name);
                project.put("description", description);
                project.put("status", status);
            }

            if (name == null || description == null || statusId == null) {
                APIResponse<Map<String, Object>> response = requestService.get(
                        "/projects/" + projectId,
                        new ParameterizedTypeReference<Map<String, Object>>() {
                        });

                if (response.getData() == null) {
                    formatterService.printError("Failed to fetch project details: " + response.getMessage());
                    return;
                }

                Map<String, Object> existingProject = response.getData();

                // Fill in missing fields with existing project data
                if (name == null) {
                    name = (String) existingProject.get("name");
                    project.put("name", name);
                }
                if (description == null) {
                    description = (String) existingProject.get("description");
                    project.put("description", description);
                }
                if (statusId == null) {
                    Map<String, Object> status = (Map<String, Object>) existingProject.get("status");
                    project.put("status", status);
                }
            }

            // Send the update request
            APIResponse<Map<String, Object>> projectResponse = requestService.put(
                    "/projects/" + projectId,
                    project,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            if (projectResponse.getData() == null) {
                formatterService.printError("Failed to update project: " + projectResponse.getMessage());
                return;
            }

            if (projectResponse.getStatusCode() == 200) {
                formatterService.printSuccess("Update successful!");
            }

        } catch (Exception e) {
            formatterService.printError("Error updating project: " + e.getMessage());
        }
    }

    @ShellMethod(key = "project-list", value = "List a user's projects")
    public void getUserProjects() {
        try {
            formatterService.printInfo("Fetching associated projects");

            APIResponse<List<Map<String, Object>>> response = requestService.get("/projects",
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    });

            List<Map<String, Object>> projects = response.getData();

            if (projects == null || projects.isEmpty()) {
                formatterService.printWarning("No associated projects");
                return;
            }

            List<String> selectedHeaders = List.of("id", "name", "description", "status", "owner_id");

            List<String> headers = new ArrayList<>(selectedHeaders);

            List<List<String>> data = projects.stream()
                    .map(row -> selectedHeaders.stream()
                            .map(key -> {
                                if (key.equals("owner_id")) {
                                    Map<String, Object> owner = (Map<String, Object>) row.get("owner");
                                    return owner != null ? String.valueOf(owner.getOrDefault("id", "N/A")) : "N/A";
                                } else if (key.equals("status")) {
                                    Map<String, Object> status = (Map<String, Object>) row.get("status");
                                    return status != null ? String.valueOf(status.getOrDefault("name", "N/A")) : "N/A";

                                } else {
                                    return String.valueOf(row.getOrDefault(key, "N/A"));
                                }
                            })
                            .toList())
                    .toList();

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            formatterService.printError("Error fetching projects: " + e.getMessage());
        }
    }

    @ShellMethod(key = "project-view-id", value = "List a user's projects with project ID")
    public void getUserProjectsById(
            @ShellOption(value = "--id", help = "Project ID") String projectId) {
        try {
            formatterService.printInfo("Fetching associated project");

            APIResponse<Map<String, Object>> response = requestService.get("/projects/" + projectId,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            if (response.getStatusCode() == 403) {
                formatterService.printWarning("No associated project found");
                return;
            }

            Map<String, Object> project = response.getData();

            List<String> selectedHeaders = List.of("id", "name", "description", "owner_id");

            List<String> headers = new ArrayList<>(selectedHeaders);

            List<List<String>> data = List.of(selectedHeaders.stream()
                    .map(key -> {
                        if (key.equals("owner_id")) {
                            // Extract from nested 'owner' field
                            Map<String, Object> owner = (Map<String, Object>) project.get("owner");
                            return owner != null ? String.valueOf(owner.getOrDefault("id", "N/A")) : "N/A";
                        } else {
                            return String.valueOf(project.getOrDefault(key, "N/A"));
                        }
                    })
                    .toList());

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            formatterService.printError("Error fetching projects: " + e.getMessage());
        }
    }

    @ShellMethod(key = "project-delete", value = "Delete a project")
    public void deleteProject(
            @ShellOption(value = "--id", help = "Project ID") String projectId) {
        try {
            requestService.delete("/projects/" + projectId);
            formatterService.printSuccess("Attempting to delete project!");
        } catch (Exception e) {
            System.err.println("Error fetching projects: " + e.getMessage());
        }
    }

}
