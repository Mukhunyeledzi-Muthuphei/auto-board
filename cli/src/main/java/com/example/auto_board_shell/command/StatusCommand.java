package com.example.auto_board_shell.command;

import com.example.auto_board_shell.service.APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class StatusCommand {

    @Autowired
    private APIService apiService;

    // project-status-view
    @ShellMethod(key = "project-status-view", value = "View all available project statuses")
    public void projectStatusView() {
        // TODO show available statuses for projects
        System.out.println("test");
    }

    // task-status-view
    @ShellMethod(key = "task-status-view", value = "View all available task statuses")
    public void taskStatusView() {
        // TODO show available statuses for tasks
        System.out.println("test");
    }

}