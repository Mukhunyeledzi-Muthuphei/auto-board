package com.example.autoboard.controller;

import com.example.autoboard.entity.Task;
import com.example.autoboard.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.autoboard.service.ActivityLogService;
import com.example.autoboard.helpers.ActionType;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ActivityLogService activityLogService;

    @Autowired
    public TaskController(TaskService taskService, ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAlltasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable Long statusId) {
        List<Task> tasks = taskService.getTasksByStatus(statusId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createTask = taskService.createTask(task);
        ActionType action = ActionType.CREATE_TASK;
        activityLogService.createLog(createTask, action.name());
        return ResponseEntity.ok(createTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task updateTask = taskService.updateTask(id, task);
        ActionType action = ActionType.UPDATE_TASK;
        activityLogService.createLog(updateTask, action.name());
        return ResponseEntity.ok(updateTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        ActionType action = ActionType.DELETE_TASK;
        activityLogService.createLog(new Task(id), action.name());
        return ResponseEntity.noContent().build();
    }

}
