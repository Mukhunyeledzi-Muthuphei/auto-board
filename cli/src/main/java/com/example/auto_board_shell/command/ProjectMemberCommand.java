package com.example.auto_board_shell.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ProjectMemberCommand {

    // project-member-add --projectId 2 --userId 5
    @ShellMethod(key = "project-member-add", value = "View comments for a task")
    public void commentsView(
            @ShellOption(value = "--projectId", help = "Project ID") String project_id,
            @ShellOption(value = "--userId", help = "User ID") String user_id
    ) {
        // TODO add a member to a project,
        // TODO need to ensure we dont have duplicate additions
        // TODO ONLY project owner can add new users to their project
        System.out.println("test");
    }

}
