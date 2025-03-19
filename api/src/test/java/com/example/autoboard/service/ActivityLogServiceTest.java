package com.example.autoboard.service;

import com.example.autoboard.entity.ActivityLog;
import com.example.autoboard.entity.Task;
import com.example.autoboard.repository.ActivityLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ActivityLogServiceTest {

    @InjectMocks
    private ActivityLogService activityLogService;

    @Mock
    private ActivityLogRepository activityLogRepository;

    @Mock
    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createLogShouldSaveActivityLog() {
        String action = "Task Created";
        when(task.getId()).thenReturn(1L);
        activityLogService.createLog(task, action);
        verify(activityLogRepository, times(1)).save(any(ActivityLog.class));
    }

    @Test
    void getLogsByTaskIdShouldReturnLogs() {
        Long taskId = 1L;
        String userId = "user123";
        ActivityLog log = new ActivityLog();
        log.setTask(task);
        log.setAction("Task Created");
        log.setTimestamp(LocalDateTime.now());
        when(activityLogRepository.findByTask(taskId, userId)).thenReturn(List.of(log));
        List<ActivityLog> logs = activityLogService.getLogsByTaskId(taskId, userId);
        assertEquals(1, logs.size());
        verify(activityLogRepository, times(1)).findByTask(taskId, userId);
    }

    @Test
    void getActivityLogsByProjectIdShouldReturnLogs() {
        Long projectId = 1L;
        String userId = "user123";
        ActivityLog log = new ActivityLog();
        log.setAction("Project Updated");
        log.setTimestamp(LocalDateTime.now());
        when(activityLogRepository.findByProjectId(projectId, userId)).thenReturn(List.of(log));
        List<ActivityLog> logs = activityLogService.getActivityLogsByProjectId(projectId, userId);
        assertEquals(1, logs.size());
        verify(activityLogRepository, times(1)).findByProjectId(projectId, userId);
    }
}