package com.example.autoboard.entity;

import com.example.autoboard.helpers.ActionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ActivityLogTest {

    private ActivityLog activityLog;
    private Task task;

    @BeforeEach
    public void setUp() {
        activityLog = new ActivityLog();
        task = new Task();
        task.setId(1L);
    }

    @Test
    public void testGettersAndSetters() {
        // Test ID
        activityLog.setId(1L);
        assertEquals(1L, activityLog.getId());

        // Test Task
        activityLog.setTask(task);
        assertEquals(task, activityLog.getTask());

        // Test ActionType
        ActionType action = ActionType.CREATE_TASK;
        activityLog.setAction(action.name());
        assertEquals(action.name(), activityLog.getAction());

        // Test Timestamp
        LocalDateTime now = LocalDateTime.now();
        activityLog.setTimestamp(now);
        assertEquals(now, activityLog.getTimestamp());
    }

    @Test
    public void testEntityProperties() {
        // Set all properties
        activityLog.setId(1L);
        activityLog.setTask(task);
        activityLog.setAction(ActionType.UPDATE_TASK.name());  // Store enum as String
        activityLog.setTimestamp(LocalDateTime.of(2025, 3, 19, 12, 0, 0, 0));

        // Assert values
        assertEquals(1L, activityLog.getId());
        assertEquals(task, activityLog.getTask());
        assertEquals(ActionType.UPDATE_TASK.name(), activityLog.getAction());
        assertEquals(LocalDateTime.of(2025, 3, 19, 12, 0, 0, 0), activityLog.getTimestamp());
    }
}