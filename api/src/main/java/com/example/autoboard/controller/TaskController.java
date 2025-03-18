package com.example.autoboard.controller;

import com.example.autoboard.entity.Task;
import com.example.autoboard.entity.User;
import com.example.autoboard.helpers.TokenHelper;
import com.example.autoboard.service.TaskService;
import com.google.api.client.util.Value;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.example.autoboard.service.ActivityLogService;
import com.example.autoboard.helpers.ActionType;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Value("${google.client.id}")
    private String clientId;
    
    private final TaskService taskService;
    private final ActivityLogService activityLogService;

    @Autowired
    public TaskController(TaskService taskService, ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAlltasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id, @RequestHeader("Authorization") String token) {

        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        Task task = taskService.getTaskById(id, new User(userId));
        return ResponseEntity.ok(task);
    }

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable Long statusId, @RequestHeader("Authorization") String token) {

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
    public ResponseEntity<Task> createTask(@RequestBody Task task, @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        if (!task.getAssignee().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Task createTask = taskService.createTask(task, new User(userId));
        ActionType action = ActionType.CREATE_TASK;
        activityLogService.createLog(createTask, action.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(createTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task, @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        Task updateTask = taskService.updateTask(id, task, new User(userId));
        if (updateTask != null) {
            ActionType action = ActionType.UPDATE_TASK;
        activityLogService.createLog(updateTask, action.name());
        return ResponseEntity.ok(updateTask);
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
        taskService.deleteTask(id, userId);
        ActionType action = ActionType.DELETE_TASK;
        activityLogService.createLog(new Task(id), action.name());
        return ResponseEntity.noContent().build();
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
