package com.example.autoboard.controller;

import com.example.autoboard.entity.ActivityLog;
import com.example.autoboard.security.JwtUtil;
import com.example.autoboard.service.ActivityLogService;
import com.google.auth.oauth2.TokenVerifier.VerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/activity-log")
public class ActivityLogController {

    private final ActivityLogService activityLogService;
    private final JwtUtil jwtUtil;

    @Autowired
    public ActivityLogController(ActivityLogService activityLogService, JwtUtil jwtUtil) {
        this.activityLogService = activityLogService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<ActivityLog> getAllLogs(@RequestHeader("Authorization") String token) throws VerificationException {
        String userId = extractUserIdFromToken(token);
        return activityLogService.getAllLogs(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityLog> getLogById(@PathVariable Long id, @RequestHeader("Authorization") String token)
            throws VerificationException {
        String userId = extractUserIdFromToken(token);
        Optional<ActivityLog> log = activityLogService.getLogById(id, userId);
        return log.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/task/{taskId}")
    public List<ActivityLog> getLogsByTaskId(@PathVariable Long taskId, @RequestHeader("Authorization") String token)
            throws VerificationException {
        String userId = extractUserIdFromToken(token);
        return activityLogService.getLogsByTaskId(taskId, userId);
    }

    private String extractUserIdFromToken(String token) throws VerificationException {
        return jwtUtil.extractUserId(token.substring(7)); // Assuming the token is in the format "Bearer <token>"
    }
}