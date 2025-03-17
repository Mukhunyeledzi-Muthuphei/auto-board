package com.example.auto_board_shell.command;

import com.example.auto_board_shell.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ShellComponent
public class ProjectCommand {

    private final RequestService requestService;
    private final FormatterService formatterService;

    @Autowired
    public ProjectCommand(RequestService requestService, FormatterService formatterService) {
        this.requestService = requestService;
        this.formatterService = formatterService;
    }

    // project-create --name "new pro" --status "2" --desc "from cli" --ownerId "google_user_101"
    @ShellMethod(key = "project-create", value = "Create a new project.")
    public void createTask(
            @ShellOption(value = "--name", help = "Project name") String name,
            @ShellOption(value = "--desc", help = "Project description") String description,
            @ShellOption(value = "--status", help = "Status ID") String statusId,
            @ShellOption(value = "--ownerId", help = "ownerId") String ownerId
    ) {
        try {
            formatterService.printInfo("Creating new project...");

            Map<String, Object> owner = new HashMap<>();
            owner.put("id", ownerId);

            Map<String, Object> project = new HashMap<>();
            project.put("name", name);
            project.put("description", description);
            project.put("statusId", statusId);
            project.put("owner", owner);

            APIResponse<Map<String, Object>> response = requestService.post(
                    "/projects",
                    project,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

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

    @ShellMethod(key = "project-list", value = "List a user's projects")
    public void getUserProjects() {
        try {
            formatterService.printInfo("Fetching associated projects");

            APIResponse<List<Map<String, Object>>> response = requestService.get("/projects", new ParameterizedTypeReference<List<Map<String, Object>>>() {});

            List<Map<String, Object>> projects = response.getData();

            List<String> selectedHeaders = List.of("id", "name", "description", "owner_id");

            List<String> headers = new ArrayList<>(selectedHeaders);

            List<List<String>> data = projects.stream()
                    .map(row -> selectedHeaders.stream()
                            .map(key -> {
                                if (key.equals("owner_id")) {
                                    // Extract from nested 'user' field
                                    Map<String, Object> owner = (Map<String, Object>) row.get("owner");
                                    return owner != null ? String.valueOf(owner.getOrDefault("id", "N/A")) : "N/A";
                                } else {
                                    return String.valueOf(row.getOrDefault(key, "N/A"));
                                }
                            })
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            formatterService.printError("Error fetching projects: " + e.getMessage());
        }
    }

    /**
     * TODO - Method should delete a project and all tasks associated
     * ONLY allow delete if JWT ID makes owner_id makes request
     */
    @ShellMethod(key = "project-delete", value = "Delete a project")
    public void deleteProject(
            @ShellOption(value = "--id", help = "Project ID") String projectId
    ) {}

}
