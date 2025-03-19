package com.example.auto_board_shell.command;

import com.example.auto_board_shell.service.APIResponse;
import com.example.auto_board_shell.service.FormatterService;
import com.example.auto_board_shell.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ShellComponent
public class StatusCommand {

    private final RequestService requestService;
    private final FormatterService formatterService;

    @Autowired
    public StatusCommand(RequestService requestService, FormatterService formatterService) {
        this.requestService = requestService;
        this.formatterService = formatterService;
    }

    // project-status-view
    @ShellMethod(key = "project-status-view", value = "View all available project statuses")
    public void projectStatusView() {
        try {
            formatterService.printInfo("Fetching available project statuses");

            APIResponse<List<Map<String, Object>>> response = requestService.get("/project-status",
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    });

            List<Map<String, Object>> statuses = response.getData();

            List<String> headers = new ArrayList<>(statuses.get(0).keySet());

            List<List<String>> data = statuses.stream()
                    .map(status -> headers.stream()
                            .map(key -> String.valueOf(status.getOrDefault(key, "N/A")))
                            .toList())
                    .toList();

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            formatterService.printError("Error fetching project statuses: " + e.getMessage());
        }
    }

    // project-status-search-id --id 2
    @ShellMethod(key = "project-status-search-id", value = "Search project status by ID")
    public void findProjectStatusById(
            @ShellOption(help = "Project Status ID") String id) {
        try {
            formatterService.printInfo("Searching for project status with ID: " + id);

            APIResponse<Map<String, Object>> response = requestService.get("/project-status/" + id,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            Map<String, Object> status = response.getData();

            if (status == null || status.isEmpty()) {
                formatterService.printWarning("No project status found for ID: " + id);
                return;
            }

            List<String> headers = new ArrayList<>(status.keySet());
            List<List<String>> data = List.of(headers.stream()
                    .map(key -> String.valueOf(status.getOrDefault(key, "N/A")))
                    .toList());

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            formatterService.printError("Error fetching project status: " + e.getMessage());
        }
    }

    // project-status-view --name "Completed"
    @ShellMethod(key = "project-status-search-name", value = "Find project status by name")
    public void findProjectStatusByName(
            @ShellOption(help = "Project Status Name") String name) {
        try {
            formatterService.printInfo("Searching for project status with name: " + name);

            APIResponse<Map<String, Object>> response = requestService.get("/project-status/name/" + name,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            Map<String, Object> status = response.getData();

            if (status == null || status.isEmpty()) {
                formatterService.printWarning("No project status found with name: " + name);
                return;
            }

            List<String> headers = new ArrayList<>(status.keySet());
            List<List<String>> data = List.of(headers.stream()
                    .map(key -> String.valueOf(status.getOrDefault(key, "N/A")))
                    .toList());

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            formatterService.printError("Error fetching project status: " + e.getMessage());
        }
    }

    // task-status-view
    @ShellMethod(key = "task-status-view", value = "View all available task statuses")
    public void taskStatusView() {
        try {
            formatterService.printInfo("Fetching available task statuses");

            APIResponse<List<Map<String, Object>>> response = requestService.get("/task-status",
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    });

            List<Map<String, Object>> statuses = response.getData();

            List<String> headers = new ArrayList<>(statuses.get(0).keySet());

            List<List<String>> data = statuses.stream()
                    .map(status -> headers.stream()
                            .map(key -> String.valueOf(status.getOrDefault(key, "N/A")))
                            .toList())
                    .toList();

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            formatterService.printError("Error fetching task statuses: " + e.getMessage());
        }
    }

    // task-status-search-id --id 2
    @ShellMethod(key = "task-status-search-id", value = "Search task status by ID")
    public void findTaskStatusById(
            @ShellOption(help = "Task Status ID") String id) {
        try {
            formatterService.printInfo("Searching for task status with ID: " + id);

            APIResponse<Map<String, Object>> response = requestService.get("/task-status/" + id,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            Map<String, Object> status = response.getData();

            if (status == null || status.isEmpty()) {
                formatterService.printWarning("No task status found for ID: " + id);
                return;
            }

            List<String> headers = new ArrayList<>(status.keySet());
            List<List<String>> data = List.of(headers.stream()
                    .map(key -> String.valueOf(status.getOrDefault(key, "N/A")))
                    .toList());

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            formatterService.printError("Error fetching task status: " + e.getMessage());
        }
    }

    // task-status-view --name "Completed"
    @ShellMethod(key = "task-status-search-name", value = "Find task status by name")
    public void findTaskStatusByName(
            @ShellOption(help = "Task Status Name") String name) {
        try {
            formatterService.printInfo("Searching for task status with name: " + name);

            APIResponse<Map<String, Object>> response = requestService.get("/task-status/name/" + name,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            Map<String, Object> status = response.getData();

            if (status == null || status.isEmpty()) {
                formatterService.printWarning("No task status found with name: " + name);
                return;
            }

            List<String> headers = new ArrayList<>(status.keySet());
            List<List<String>> data = List.of(headers.stream()
                    .map(key -> String.valueOf(status.getOrDefault(key, "N/A")))
                    .toList());

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            formatterService.printError("Error fetching task status: " + e.getMessage());
        }
    }

}