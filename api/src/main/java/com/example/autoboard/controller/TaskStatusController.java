package com.example.autoboard.controller;

import com.example.autoboard.entity.TaskStatus;
import com.example.autoboard.service.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-status")
public class TaskStatusController {

    @Autowired
    private TaskStatusService taskStatusService;

    @GetMapping
    public List<TaskStatus> getAllProjects() {
        return taskStatusService.getAllTaskStatuses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskStatus> getProjectById(@PathVariable Long id) {
        TaskStatus taskStatus = taskStatusService.getTaskStatusById(id);
        if (taskStatus != null) {
            return ResponseEntity.ok(taskStatus);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // TODO - Remove / keep - but this is only for testing purposes for now
    @PostMapping
    public TaskStatus createTaskStatus(@RequestBody TaskStatus taskStatus) {
        return taskStatusService.createTaskStatus(taskStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskStatus(@PathVariable Long id) {
        boolean isDeleted = taskStatusService.deleteTaskStatus(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
