package com.example.autoboard.controller;

import com.example.autoboard.helpers.TokenHelper;
import com.example.autoboard.service.TaskService;
import com.google.api.client.util.Value;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.example.autoboard.service.ActivityLogService;
import com.example.autoboard.repository.*;
import com.example.autoboard.helpers.ActionType;
import com.example.autoboard.entity.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Value("${google.client.id}")
    private String clientId;

    private final TaskService taskService;
    private final ActivityLogService activityLogService;
    private final ProjectRepository projectRepository;

    @Autowired
    public TaskController(TaskService taskService, ActivityLogService activityLogService,
            ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.activityLogService = activityLogService;
        this.taskService = taskService;
    }

    // Return all tasks for all projects that a user has access to, either owner or
    // project member
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestHeader("Authorization") String token) {
        // Validate token
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Extract user ID
        String userId = TokenHelper.extractUserIdFromToken(token);

        // Task service to run SQL
        List<Task> tasks = taskService.getTasksAvailableToUser(userId);

        // Check if null
        if (tasks == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        // Return tasks
        return ResponseEntity.ok(tasks);
    }

    // Return tasks assigned to a user
    @GetMapping("/assigned")
    public ResponseEntity<List<Task>> getTasksByUserId(
            @RequestHeader("Authorization") String token) {
        // Validate token
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Extract user ID
        String userId = TokenHelper.extractUserIdFromToken(token);

        // Task service to run SQL
        List<Task> tasks = taskService.getTasksByUserId(userId);

        // Check if null
        if (tasks == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        // Return tasks
        return ResponseEntity.ok(tasks);
    }

    // Return task if user is project member or owner
    @GetMapping("/project/{id}")
    public ResponseEntity<List<Task>> getTaskByProjectId(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        // Validate token
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Extract user ID
        String userId = TokenHelper.extractUserIdFromToken(token);

        // Task service to run SQL
        List<Task> tasks = taskService.getTasksByProjectId(id, userId);

        // Check if null
        if (tasks == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        // Return tasks
        return ResponseEntity.ok(tasks);
    }

    // Return task if user is project member or owner
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        // Validate token
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Extract user ID
        String userId = TokenHelper.extractUserIdFromToken(token);

        // Get task by ID if user has access
        Optional<Task> task = taskService.getTasksById(id, userId);

        // Return task or not found status
        return task.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable Long statusId,
            @RequestHeader("Authorization") String token) {

        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        User assignee = new User();
        assignee.setId(userId);
        List<Task> tasks = taskService.getTasksByStatus(statusId, new User(userId));
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestBody Task task,
            @RequestHeader("Authorization") String token) {
        // Validate the token
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = TokenHelper.extractUserIdFromToken(token);
        if (!task.getAssignee().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Optional<Project> project = projectRepository.findProjectByIdAndUser(task.getProject().getId(), userId);

        if (project.isPresent()) {
            task.setProject(project.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Ensure the task is associated with a valid project
        if (task.getProject() == null || task.getProject().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Task createdTask = taskService.createTask(task);

        // Log the activity
        ActionType action = ActionType.CREATE_TASK;
        activityLogService.createLog(createdTask, action.name());

        // Return the created task with a 201 CREATED status
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task,
            @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.notFound().build();
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        Task updatedTask = taskService.updateTask(id, task, userId);
        if (updatedTask != null) {
            ActionType action = ActionType.UPDATE_TASK;
            if (updatedTask.getStatus().getId() == 4) {
                action = ActionType.COMPLETE_TASK;
            }
            activityLogService.createLog(updatedTask, action.name());
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        boolean isDeleted = taskService.deleteTask(id, userId);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{taskId}/assign")
    public ResponseEntity<Task> assignTask(
            @PathVariable Long taskId,
            @RequestBody Map<String, Object> requestBody,
            @RequestHeader("Authorization") String token) {

        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // Extract the assignee ID from the request body
        Map<String, Object> assigneeMap = (Map<String, Object>) requestBody.get("assignee");
        String assigneeId = (String) assigneeMap.get("id");
        Task assignedTask = taskService.assignTask(taskId, assigneeId);

        if (assignedTask != null) {
            return ResponseEntity.ok(assignedTask);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
