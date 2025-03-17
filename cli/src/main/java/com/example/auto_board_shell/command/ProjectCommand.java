package com.example.auto_board_shell.command;

import com.example.auto_board_shell.service.APIService;
import com.example.auto_board_shell.service.ShellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.auto_board_shell.helpers.CurrentUser;

@ShellComponent
public class ProjectCommand {

    @Autowired
    private APIService apiService;

    @Autowired
    private ShellService shellService;

    // project-create --name "new pro" --status "2" --desc "from cli" --ownerId "1"
    @ShellMethod(key = "project-create", value = "Create a new project.")
    public void createTask() {
        try {
            shellService.printHeading("Creating new project...");

            String name = shellService.prompt("Enter project name: ");
            String description = shellService.prompt("Enter project description: ");

            Map<String, Object> owner = new HashMap<>();
            owner.put("id", CurrentUser.getId());

            Map<String, Object> status = new HashMap<>();
            status.put("id", "1");

            Map<String, Object> project = new HashMap<>();
            project.put("name", name);
            project.put("description", description);
            project.put("status", status);
            project.put("owner", owner);

            Object createdProject = apiService.post("/projects", project, Object.class);
            shellService.printSuccess("Project created successfully!");

            // Display the created task
            @SuppressWarnings("unchecked")
            Map<String, Object> ProjectResult = (Map<String, Object>) createdProject;
            shellService.printInfo("ID: " + ProjectResult.get("id"));
            shellService.printInfo("Name: " + ProjectResult.get("name"));

        } catch (Exception e) {
            shellService.printError("Error creating project: " + e.getMessage());
        }
    }

    @ShellMethod(key = "project-list", value = "List a user's projects")
    public void getUserProjects() {
        try {
            // Fetch projects as a list of generic maps
            List<Map<String, Object>> projects = apiService.get("/projects", List.class);

            if (projects != null && !projects.isEmpty()) {
                System.out.println("User Projects:");
                for (Object projectObj : projects) {
                    Map<String, Object> project = (Map<String, Object>) projectObj; // Cast each item to a Map
                    System.out.println(project);
                }
            } else {
                System.out.println("No projects found.");
            }
        } catch (Exception e) {
            System.err.println("Error fetching projects: " + e.getMessage());
        }
    }

    /**
     * TODO - Method should delete a project and all tasks associated
     * ONLY allow delete if JWT ID makes owner_id makes request
     */
    @ShellMethod(key = "project-delete", value = "Delete a project")
    public void deleteProject(
            @ShellOption(value = "--id", help = "Project ID") String projectId) {
        try {
            apiService.delete("/projects/" + projectId, Object.class);
            shellService.printSuccess("Project deleted successfully!");
        } catch (Exception e) {
            System.err.println("Error fetching projects: " + e.getMessage());
        }
    }

}
