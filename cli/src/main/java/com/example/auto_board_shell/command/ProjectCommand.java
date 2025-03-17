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

            Object createdProject = requestService.post("/projects", project, Object.class);
            Map<String, Object> projectResult = (Map<String, Object>) createdProject;
            formatterService.printInfo("ID: " + projectResult.get("id"));
            formatterService.printInfo("Name: " + projectResult.get("name"));

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

            List<String> headers = new ArrayList<>(projects.get(0).keySet());

            List<List<String>> data = projects.stream()
                    .map(status -> headers.stream()
                            .map(key -> String.valueOf(status.getOrDefault(key, "N/A")))
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
