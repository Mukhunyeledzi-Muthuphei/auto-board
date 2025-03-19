package com.example.autoboard.controller;

import com.example.autoboard.entity.ActivityLog;
import com.example.autoboard.service.ActivityLogService;
import com.google.auth.oauth2.TokenVerifier.VerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import com.example.autoboard.helpers.TokenHelper;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/activity-log")
public class ActivityLogController {

    @Value("${google.client.id}")
    private String clientId;

    private final ActivityLogService activityLogService;

    @Autowired
    public ActivityLogController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @Deprecated
    @GetMapping
    public List<ActivityLog> getAllLogs(@RequestHeader("Authorization") String token) throws VerificationException {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return List.of();
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        return activityLogService.getAllLogs(userId);
    }

    @Deprecated
    @GetMapping("/{id}")
    public ResponseEntity<ActivityLog> getLogById(@PathVariable Long id, @RequestHeader("Authorization") String token)
            throws VerificationException {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.notFound().build();
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        Optional<ActivityLog> log = activityLogService.getLogById(id, userId);
        return log.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<ActivityLog>> getLogsByTaskId(@PathVariable Long taskId,
            @RequestHeader("Authorization") String token
    ) throws VerificationException {
        // Validate token
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(401).body(List.of()); // Unauthorized
        }
        // Extract user ID
        String userId = TokenHelper.extractUserIdFromToken(token);
        // Get activity logs for a task
        List<ActivityLog> logs = activityLogService.getLogsByTaskId(taskId, userId);
        if (logs.isEmpty()) {
            return ResponseEntity.status(404).body(List.of()); // Not Found
        }
        return ResponseEntity.ok(logs); // Success
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ActivityLog>> getActivityLogsByProjectId(@PathVariable Long projectId,
            @RequestHeader("Authorization") String token
    ) throws VerificationException {
        // Validate token
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(401).body(List.of()); // Unauthorized
        }
        // Extract user ID
        String userId = TokenHelper.extractUserIdFromToken(token);
        // Get activity logs for all tasks within a project
        List<ActivityLog> logs = activityLogService.getActivityLogsByProjectId(projectId, userId);
        if (logs.isEmpty()) {
            return ResponseEntity.status(404).body(List.of()); // Not Found
        }
        return ResponseEntity.ok(logs); // Success
    }

}