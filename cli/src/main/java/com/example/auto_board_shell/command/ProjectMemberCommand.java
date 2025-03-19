package com.example.auto_board_shell.command;

import java.util.HashMap;
import java.util.Map;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.example.auto_board_shell.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.example.auto_board_shell.service.APIResponse;
import com.example.auto_board_shell.service.FormatterService;

@ShellComponent
public class ProjectMemberCommand {

    @Autowired
    private final RequestService requestService;

    @Autowired
    private final FormatterService formatterService;

    public ProjectMemberCommand(RequestService requestService, FormatterService formatterService) {
        this.formatterService = formatterService;
        this.requestService = requestService;
    }

    // project-member-add --projectId 2 --userId 5
    @ShellMethod(key = "project-member-add", value = "View comments for a task")
    public void addProjectMember(
            @ShellOption(value = "--projectId", help = "Project ID") String projectId,
            @ShellOption(value = "--userId", help = "User ID") String userId) {

        Map<String, Object> project = new HashMap<>();
        project.put("id", projectId);

        Map<String, Object> user = new HashMap<>();
        user.put("id", userId);

        Map<String, Object> projectMember = new HashMap<>();
        projectMember.put("project", project);
        projectMember.put("user", user);

        requestService.post("/project-members", projectMember, new ParameterizedTypeReference<Map<String, Object>>() {
        });
    }

    // project-member-remove --projectId 2 --userId 5
    @ShellMethod(key = "project-member-remove", value = "View comments for a task")
    public void removeProjectMember(
            @ShellOption(value = "--projectId", help = "Project ID") String projectId,
            @ShellOption(value = "--userId", help = "User ID") String userId) {

        Map<String, Object> project = new HashMap<>();
        project.put("id", projectId);

        Map<String, Object> user = new HashMap<>();
        user.put("id", userId);

        Map<String, Object> projectMember = new HashMap<>();
        projectMember.put("project", project);
        projectMember.put("user", user);

        requestService.delete("/project-members", projectMember);
    }

    // project-member-list --projectId 2
    @ShellMethod(key = "project-member-list", value = "View comments for a task")
    public void listProjectMembers(
            @ShellOption(value = "--projectId", help = "Project ID") String project_id) {

        // Correct the ParameterizedTypeReference to match the expected response type
        APIResponse<List<Map<String, Object>>> response = requestService.get(
                "/project-members/project/" + project_id,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                });

        List<Map<String, Object>> membersList = response.getData();

        List<String> selectedHeaders = List.of("project_member_id", "project_id", "user_id");

        List<String> headers = new ArrayList<>(selectedHeaders);

        List<List<String>> data = membersList.stream()
                .map(row -> selectedHeaders.stream()
                        .map(key -> {
                            switch (key) {
                                case "project_member_id":
                                    // Extract the top-level "id" field
                                    return String.valueOf(row.getOrDefault("id", "N/A"));
                                case "project_id":
                                    // Extract the "id" field from the nested "project" object
                                    Map<String, Object> project = (Map<String, Object>) row.get("project");
                                    return project != null
                                            ? String.valueOf(project.getOrDefault("id", "N/A"))
                                            : "N/A";
                                case "user_id":
                                    // Extract the "id" field from the nested "user" object
                                    Map<String, Object> user = (Map<String, Object>) row.get("user");
                                    return user != null
                                            ? String.valueOf(user.getOrDefault("id", "N/A"))
                                            : "N/A";
                                default:
                                    return "N/A";
                            }
                        })
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        formatterService.printTable(headers, data);

    }

}
