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
public class ActivityLogCommand {

    private final RequestService requestService;
    private final FormatterService formatterService;

    private static final String TASK_ID = "task_id";
    private static final String TITLE = "title";

    @Autowired
    public ActivityLogCommand(RequestService requestService, FormatterService formatterService) {
        this.requestService = requestService;
        this.formatterService = formatterService;
    }

    // activity-log-project --id 2
    @ShellMethod(key = "activity-log-project", value = "View all task logs for a project")
    public void activityLogProject(
            @ShellOption(value = "--id", help = "Project id") String id) {
        try {
            formatterService.printInfo("Fetching project activity log");

            APIResponse<List<Map<String, Object>>> response = requestService.get("/activity-log/project/" + id,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    });

            List<Map<String, Object>> projectActivityLog = response.getData();

            if (projectActivityLog == null || projectActivityLog.isEmpty()) {
                formatterService.printWarning("No project activity log found");
                return;
            }

            List<String> selectedHeaders = List.of(TASK_ID, TITLE, "action", "timestamp", "id");

            List<String> headers = new ArrayList<>(selectedHeaders);

            List<List<String>> data = projectActivityLog.stream()
                    .map(row -> selectedHeaders.stream()
                            .map(key -> {
                                if (key.equals(TASK_ID) || key.equals(TITLE)) {
                                    // Extract from nested 'task' field
                                    Map<String, Object> task = (Map<String, Object>) row.get("task");
                                    return task != null
                                            ? String.valueOf(
                                                    task.getOrDefault(key.equals(TASK_ID) ? "id" : TITLE, "N/A"))
                                            : "N/A";
                                } else {
                                    return String.valueOf(row.getOrDefault(key, "N/A"));
                                }
                            })
                            .toList())
                    .toList();

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            formatterService.printError("Error fetching project activity log: " + e.getMessage());
        }
    }

    // activity-log-task --id 2
    @ShellMethod(key = "activity-log-task", value = "View task logs")
    public void activityLogTask(
            @ShellOption(value = "--id", help = "Task id") String id) {
        try {
            formatterService.printInfo("Fetching task activity log");

            APIResponse<List<Map<String, Object>>> response = requestService.get("/activity-log/task/" + id,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    });

            List<Map<String, Object>> taskActivityLog = response.getData();

            if (taskActivityLog == null || taskActivityLog.isEmpty()) {
                formatterService.printWarning("No task activity log found");
                return;
            }

            List<String> selectedHeaders = List.of(TASK_ID, TITLE, "action", "timestamp", "id");

            List<String> headers = new ArrayList<>(selectedHeaders);

            List<List<String>> data = taskActivityLog.stream()
                    .map(row -> selectedHeaders.stream()
                            .map(key -> {
                                if (key.equals(TASK_ID) || key.equals(TITLE)) {
                                    // Extract from nested 'task' field
                                    Map<String, Object> task = (Map<String, Object>) row.get("task");
                                    return task != null
                                            ? String.valueOf(
                                                    task.getOrDefault(key.equals(TASK_ID) ? "id" : TITLE, "N/A"))
                                            : "N/A";
                                } else {
                                    return String.valueOf(row.getOrDefault(key, "N/A"));
                                }
                            })
                            .toList())
                    .toList();

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            formatterService.printError("Error fetching task activity log: " + e.getMessage());
        }
    }
}
