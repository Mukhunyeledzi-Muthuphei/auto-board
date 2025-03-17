package com.example.autoboard.controller;

import com.example.autoboard.entity.Task;
import com.example.autoboard.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaim("sub");
        return taskService.getAlltasks(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaim("sub");
        Task task = taskService.getTaskById(id, userId);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable Long statusId, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaim("sub");
        List<Task> tasks = taskService.getTasksByStatus(statusId, userId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaim("sub");
        Task createTask = taskService.createTask(task, userId);
        return ResponseEntity.ok(createTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaim("sub");
        Task updateTask = taskService.updateTask(id, task, userId);
        return ResponseEntity.ok(updateTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaim("sub");
        taskService.deleteTask(id, userId);
        return ResponseEntity.noContent().build();
    }

}
