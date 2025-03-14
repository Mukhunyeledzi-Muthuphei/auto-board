package com.example.autoboard.controller;

import com.example.autoboard.entity.ActivityLog;
import com.example.autoboard.service.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/activity-log")
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @Autowired
    public ActivityLogController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @GetMapping
    public List<ActivityLog> getAllLogs() {
        return activityLogService.getAllLogs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityLog> getLogById(@PathVariable Long id) {
        Optional<ActivityLog> log = activityLogService.getLogById(id);
        return log.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/task/{taskId}")
    public List<ActivityLog> getLogsByTaskId(@PathVariable Long taskId) {
        return activityLogService.getLogsByTaskId(taskId);
    }

    // TODO REMOVE LATER
    // @PostMapping
    // public ResponseEntity<ActivityLog> createLog(@RequestBody ActivityLog log) {
    // ActivityLog newLog = activityLogService.createLog(log.getTaskId(),
    // log.getAction());
    // return ResponseEntity.ok(newLog);
    // }
}