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

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Value("${google.client.id}")
    private String clientId;
    
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
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
            return ResponseEntity.notFound().build();
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        Task task = taskService.getTaskById(id, new User(userId));
        return ResponseEntity.ok(task);
    }

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable Long statusId, @RequestHeader("Authorization") String token) {

        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.notFound().build();
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
        return ResponseEntity.status(HttpStatus.CREATED).body(createTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task, @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.notFound().build();
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        Task updateTask = taskService.updateTask(id, task, new User(userId));
        if (updateTask != null) {
            return ResponseEntity.ok(updateTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.notFound().build();
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        taskService.deleteTask(id, userId);
        return ResponseEntity.noContent().build();
    }

}
