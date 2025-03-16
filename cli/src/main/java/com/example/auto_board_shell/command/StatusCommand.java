package com.example.auto_board_shell.command;

import com.example.auto_board_shell.service.APIService;
import com.example.auto_board_shell.service.FormatterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ShellComponent
public class StatusCommand {

    private final APIService apiService;
    private final FormatterService formatterService;

    @Autowired
    public StatusCommand(APIService apiService, FormatterService formatterService) {
        this.apiService = apiService;
        this.formatterService = formatterService;
    }

    // project-status-view
    @ShellMethod(key = "project-status-view", value = "View all available project statuses")
    public void projectStatusView() {
        try {
            List<Map<String, Object>> statuses = apiService.get("/project-status", List.class);

            if (statuses == null || statuses.isEmpty()) {
                System.out.println("No project statuses found.");
                return;
            }

            List<String> headers = new ArrayList<>(statuses.get(0).keySet());

            List<List<String>> data = statuses.stream()
                    .map(status -> headers.stream()
                            .map(key -> String.valueOf(status.getOrDefault(key, "N/A")))
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            System.out.println("Error fetching project statuses: " + e.getMessage());
        }
    }

    // project-status-view --id 2
    @ShellMethod(key = "project-status-search-id", value = "Search project status by ID")
    public void findProjectStatusById() {
        // TODO show available statuses for projects
        System.out.println("test");
    }

    // project-status-view --name "Completed"
    @ShellMethod(key = "project-status-search-name", value = "Find project status by name")
    public void findProjectStatusByName() {
        // TODO show available statuses for projects
        System.out.println("test");
    }

    // task-status-view
    @ShellMethod(key = "task-status-view", value = "View all available task statuses")
    public void taskStatusView() {
        try {
            List<Map<String, Object>> statuses = apiService.get("/task-status", List.class);

            if (statuses == null || statuses.isEmpty()) {
                System.out.println("No task statuses found.");
                return;
            }

            List<String> headers = new ArrayList<>(statuses.get(0).keySet());

            List<List<String>> data = statuses.stream()
                    .map(status -> headers.stream()
                            .map(key -> String.valueOf(status.getOrDefault(key, "N/A")))
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            System.out.println("Error fetching task statuses: " + e.getMessage());
        }
    }

}