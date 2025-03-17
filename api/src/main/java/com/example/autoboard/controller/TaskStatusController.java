package com.example.autoboard.controller;

import com.example.autoboard.entity.TaskStatus;
import com.example.autoboard.service.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/task-status")
public class TaskStatusController {

    @Autowired
    private TaskStatusService taskStatusService;

    @GetMapping
    public List<TaskStatus> getAllTaskStatuses() {
        return taskStatusService.getAllTaskStatuses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskStatus> getTaskStatusById(@PathVariable Long id) {
        Optional<TaskStatus> taskStatus = taskStatusService.getTaskStatusById(id);
        return taskStatus.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<TaskStatus> getTaskStatusByName(@PathVariable String name) {
        TaskStatus taskStatus = taskStatusService.getTaskStatusByName(name);
        return taskStatus != null ? ResponseEntity.ok(taskStatus) : ResponseEntity.notFound().build();
    }

}
