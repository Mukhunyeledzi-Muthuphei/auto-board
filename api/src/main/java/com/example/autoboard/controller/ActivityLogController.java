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

    @GetMapping
    public List<ActivityLog> getAllLogs(@RequestHeader("Authorization") String token) throws VerificationException {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return List.of();
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        return activityLogService.getAllLogs(userId);
    }

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
    public List<ActivityLog> getLogsByTaskId(@PathVariable Long taskId, @RequestHeader("Authorization") String token)
            throws VerificationException {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return List.of();
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        return activityLogService.getLogsByTaskId(taskId, userId);
    }
}