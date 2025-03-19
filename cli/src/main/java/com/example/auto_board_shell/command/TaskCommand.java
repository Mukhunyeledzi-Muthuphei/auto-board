package com.example.auto_board_shell.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import com.example.auto_board_shell.service.RequestService;
import com.example.auto_board_shell.service.FormatterService;
import com.example.auto_board_shell.service.APIResponse;

import java.util.*;
import java.util.stream.Collectors;

import com.example.auto_board_shell.helpers.CurrentUser;

@ShellComponent
public class TaskCommand {

    private final RequestService requestService;
    private final FormatterService formatterService;

    @Autowired
    public TaskCommand(RequestService requestService, FormatterService formatterService) {
        this.requestService = requestService;
        this.formatterService = formatterService;
    }

    @ShellMethod(key = "task-create", value = "Create a new task for a project.")
    public void createTask(
            @ShellOption(value = "--projectId", help = "Project ID") String project_id) {
        try {
            formatterService.printInfo("Creating new task...");

            String title = formatterService.prompt("Enter task title: ");
            String description = formatterService.prompt("Enter task description: ");

            Map<String, Object> assignee = new HashMap<>();
            assignee.put("id", CurrentUser.getId());

            Map<String, Object> status = new HashMap<>();
            status.put("id", "1");

            Map<String, Object> project = new HashMap<>();
            project.put("id", project_id);

            Map<String, Object> task = new HashMap<>();
            task.put("title", title);
            task.put("description", description);
            task.put("status", status);
            task.put("assignee", assignee);
            task.put("project", project);

            APIResponse<Map<String, Object>> response = requestService.post(
                    "/tasks",
                    task,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            if (response.getStatusCode() == 404) {
                formatterService.printError("No projects available with ID: " + project_id);
                return;
            }

            if (response.getData() == null) {
                formatterService.printError("Failed to create task: " + response.getMessage());
                return;
            }

            Object data = response.getData();
            if (data instanceof Map) {
                Map<String, Object> taskResult = (Map<String, Object>) data;
                formatterService.printInfo("ID: " + taskResult.get("id"));
                formatterService.printInfo("Title: " + taskResult.get("title"));
            } else {
                formatterService.printError("Unexpected response type: " + data.getClass().getName());
            }

        } catch (Exception e) {
            formatterService.printError("Error creating task: " + e.getMessage());
        }
    }

    @ShellMethod(key = "task-update", value = "Update a task.")
    public void updateTask(
            @ShellOption(value = "--id", help = "Task ID") String taskId,
            @ShellOption(value = "--title", help = "Task title", defaultValue = ShellOption.NULL) String title,
            @ShellOption(value = "--description", help = "Task description", defaultValue = ShellOption.NULL) String description,
            @ShellOption(value = "--status", help = "Task status", defaultValue = ShellOption.NULL) String statusId) {
        try {
            formatterService.printInfo("Updating task...");

            // Fetch the existing task details
            APIResponse<Map<String, Object>> existingTaskResponse = requestService.get(
                    "/tasks/" + taskId,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            Map<String, Object> existingTaskData = existingTaskResponse.getData();

            if (existingTaskData == null) {
                formatterService.printError("Failed to fetch existing task: " + existingTaskResponse.getMessage());
                return;
            }

            Map<String, Object> existingTask = existingTaskResponse.getData();

            // Extract the existing project
            Map<String, Object> existingProject = (Map<String, Object>) existingTask.get("project");
            if (existingProject == null) {
                formatterService.printError("Existing task does not have a project assigned.");
                return;
            }

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

            if (title == null && description == null && statusId == null) {
                formatterService.printTable(headers, data);
                title = formatterService.prompt("Enter task title: ");
                description = formatterService.prompt("Enter task description: ");
                statusId = formatterService.prompt("Enter status Id: ");
            }

            Map<String, Object> status = new HashMap<>();
            if (statusId != null) {
                long newStatusId = Integer.valueOf(statusId);
                status.put("id", newStatusId);
            }

            if (title != null) {
                existingTask.put("title", title);
            }
            if (description != null) {
                existingTask.put("description", description);
            }
            if (statusId != null) {
                existingTask.put("status", status);
            }
            existingTask.put("project", existingProject); // Include the existing project;

            APIResponse<Map<String, Object>> taskResponse = requestService.put(
                    "/tasks/" + taskId,
                    existingTask,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            if (taskResponse.getData() == null) {
                formatterService.printError("Failed to update task: " + taskResponse.getMessage());
                return;
            }

            if (taskResponse.getStatusCode() == 200) {
                formatterService.printSuccess("Update successful!");
            }

        } catch (Exception e) {
            formatterService.printError("Error updating task: " + e.getMessage());
        }
    }

    @ShellMethod(key = "task-delete", value = "Delete a task.")
    public void deleteTask(
            @ShellOption(value = "--id", help = "Task ID") String taskId) {
        try {
            requestService.delete("/tasks/" + taskId);
            formatterService.printSuccess("Task deleted successfully!");
        } catch (Exception e) {
            formatterService.printError("Error deleting task: " + e.getMessage());
        }
    }

    @ShellMethod(key = "task-list-all", value = "List all tasks from all projects where the user is a part of.")
    public void listAllTasks() {
        try {
            formatterService.printInfo("Fetching all tasks...");

            APIResponse<List<Map<String, Object>>> response = requestService.get("/tasks",
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    });

            List<Map<String, Object>> tasks = response.getData();

            if (tasks == null || tasks.isEmpty()) {
                formatterService.printWarning("No tasks found");
                return;
            }

            List<String> selectedHeaders = List.of("id", "title", "description", "status", "assignee_id", "project_id");

            List<String> headers = new ArrayList<>(selectedHeaders);

            List<List<String>> data = tasks.stream()
                    .map(row -> selectedHeaders.stream()
                            .map(key -> {
                                if (key.equals("assignee_id")) {
                                    Map<String, Object> assignee = (Map<String, Object>) row.get("assignee");
                                    return assignee != null ? String.valueOf(assignee.getOrDefault("id", "N/A"))
                                            : "N/A";
                                } else if (key.equals("project_id")) {
                                    Map<String, Object> project = (Map<String, Object>) row.get("project");
                                    return project != null ? String.valueOf(project.getOrDefault("id", "N/A")) : "N/A";
                                } else if (key.equals("status")) {
                                    Map<String, Object> status = (Map<String, Object>) row.get("status");
                                    return status != null ? String.valueOf(status.getOrDefault("name", "N/A")) : "N/A";
                                } else {
                                    return String.valueOf(row.getOrDefault(key, "N/A"));
                                }
                            })
                            .toList())
                    .toList();

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            formatterService.printError("Error fetching tasks: " + e.getMessage());
        }
    }

    @ShellMethod(key = "task-list-assigned", value = "List all tasks assigned to the user.")
    public void listAssignedTasks() {
        try {
            formatterService.printInfo("Fetching assigned tasks...");

            APIResponse<List<Map<String, Object>>> response = requestService.get("/tasks/assigned",
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    });

            List<Map<String, Object>> tasks = response.getData();

            if (tasks == null || tasks.isEmpty()) {
                formatterService.printWarning("No tasks found");
                return;
            }

            List<String> selectedHeaders = List.of("id", "title", "description", "status", "project_id");

            List<String> headers = new ArrayList<>(selectedHeaders);

            List<List<String>> data = tasks.stream()
                    .map(row -> selectedHeaders.stream()
                            .map(key -> {
                                if (key.equals("status")) {
                                    Map<String, Object> status = (Map<String, Object>) row.get("status");
                                    return status != null ? String.valueOf(status.getOrDefault("name", "N/A")) : "N/A";
                                } else if (key.equals("project_id")) {
                                    Map<String, Object> project = (Map<String, Object>) row.get("project");
                                    return project != null ? String.valueOf(project.getOrDefault("id", "N/A")) : "N/A";
                                } else {
                                    return String.valueOf(row.getOrDefault(key, "N/A"));
                                }
                            })
                            .toList())
                    .toList();

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            formatterService.printError("Error fetching tasks: " + e.getMessage());
        }
    }

    @ShellMethod(key = "task-search-id", value = "Search for a task by ID")
    public void findTaskById(
            @ShellOption(value = "--taskId", help = "Task ID") String taskId) {
        try {
            formatterService.printInfo("Searching for task...");

            APIResponse<Map<String, Object>> response = requestService.get("/tasks/" + taskId,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            Map<String, Object> task = response.getData();

            if (task == null || task.isEmpty()) {
                formatterService.printWarning("No tasks found");
                return;
            }

            List<String> selectedHeaders = List.of("id", "title", "description", "status", "assignee_id", "project_id");

            List<String> headers = new ArrayList<>(selectedHeaders);

            List<List<String>> data = List.of(selectedHeaders.stream()
                    .map(key -> {
                        if (key.equals("assignee_id")) {
                            Map<String, Object> assignee = (Map<String, Object>) task.get("assignee");
                            return assignee != null ? String.valueOf(assignee.getOrDefault("id", "N/A")) : "N/A";
                        } else if (key.equals("status")) {
                            Map<String, Object> status = (Map<String, Object>) task.get("status");
                            return status != null ? String.valueOf(status.getOrDefault("name", "N/A")) : "N/A";
                        } else if (key.equals("project_id")) {
                            Map<String, Object> projectId = (Map<String, Object>) task.get("project");
                            return projectId != null ? String.valueOf(projectId.getOrDefault("id", "N/A")) : "N/A";
                        } else {
                            return String.valueOf(task.getOrDefault(key, "N/A"));
                        }
                    })
                    .toList());

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            formatterService.printError("Error fetching tasks: " + e.getMessage());
        }
    }

    @ShellMethod(key = "task-list-project", value = "List all tasks for a specific project.")
    public void listTasksByProject(
            @ShellOption(value = "--project-id", help = "Project ID") String projectId) {
        try {
            formatterService.printInfo("Fetching tasks for project...");

            APIResponse<List<Map<String, Object>>> response = requestService.get("/tasks/project/" + projectId,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    });

            List<Map<String, Object>> tasks = response.getData();

            if (tasks == null || tasks.isEmpty()) {
                formatterService.printWarning("No tasks found");
                return;
            }

            List<String> selectedHeaders = List.of("id", "title", "description", "status", "assignee_id", "project_id");

            List<String> headers = new ArrayList<>(selectedHeaders);

            List<List<String>> data = tasks.stream()
                    .map(row -> selectedHeaders.stream()
                            .map(key -> {
                                if (key.equals("assignee_id")) {
                                    Map<String, Object> assignee = (Map<String, Object>) row.get("assignee");
                                    return assignee != null ? String.valueOf(assignee.getOrDefault("id", "N/A"))
                                            : "N/A";
                                } else if (key.equals("project_id")) {
                                    Map<String, Object> project = (Map<String, Object>) row.get("project");
                                    return project != null ? String.valueOf(project.getOrDefault("id", "N/A")) : "N/A";
                                } else if (key.equals("status")) {
                                    Map<String, Object> status = (Map<String, Object>) row.get("status");
                                    return status != null ? String.valueOf(status.getOrDefault("name", "N/A")) : "N/A";
                                } else {
                                    return String.valueOf(row.getOrDefault(key, "N/A"));
                                }
                            })
                            .toList())
                    .toList();

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            formatterService.printError("Error fetching tasks: " + e.getMessage());
        }
    }

    @ShellMethod(key = "task-assign", value = "Assign a task to a project member.")
    public void assignTask(
            @ShellOption(value = "--taskId", help = "Task ID") String taskId,
            @ShellOption(value = "--assigneeId", help = "Assignee ID") String assigneeId) {
        try {
            formatterService.printInfo("Assigning task...");

            Map<String, Object> assignee = new HashMap<>();
            assignee.put("id", assigneeId);

            Map<String, Object> task = new HashMap<>();
            task.put("assignee", assignee);

            APIResponse<Map<String, Object>> response = requestService.put(
                    "/tasks/" + taskId + "/assign",
                    task,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });
            if (response.getData() == null) {
                formatterService.printError("Failed to assign task: " + response.getMessage());
                return;
            }
            if (response.getStatusCode() == 200) {
                formatterService.printSuccess("Task assigned successfully!");
            }
        } catch (Exception e) {
            formatterService.printError("Error assigning task: " + e.getMessage());
        }
    }

}
