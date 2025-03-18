package com.example.auto_board_shell.command;

import java.util.HashMap;
import java.util.Map;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.example.auto_board_shell.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;

@ShellComponent
public class ProjectMemberCommand {

    @Autowired
    private final RequestService requestService;

    // project-member-add --projectId 2 --userId 5
    @ShellMethod(key = "project-member-add", value = "View comments for a task")
    public void commentsView(
            @ShellOption(value = "--projectId", help = "Project ID") String project_id,
            @ShellOption(value = "--userId", help = "User ID") String user_id) {
        // TODO add a member to a project,

        Map<String, Object> project = new HashMap<>();
        project.put("id", project_id);

        Map<String, Object> user = new HashMap<>();
        user.put("id", user_id);

        Map<String, Object> projectMember = new HashMap<>();
        projectMember.put("project", project);
        projectMember.put("user", user);

        requestService.post("/project-members", projectMember, new ParameterizedTypeReference<Map<String, Object>>() {
        });
        // TODO ONLY project owner can add new users to their project
        // System.out.println("test");
    }

}
