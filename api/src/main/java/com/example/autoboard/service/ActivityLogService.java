package com.example.autoboard.service;

import com.example.autoboard.entity.ActivityLog;
import com.example.autoboard.entity.Task;
import com.example.autoboard.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    @Autowired
    public ActivityLogService(
            ActivityLogRepository activityLogRepository
    ) {
        this.activityLogRepository = activityLogRepository;
    }

    public void createLog(Task task, String action) {
        ActivityLog log = new ActivityLog();
        log.setTask(task);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());
        activityLogRepository.save(log);
    }

    public List<ActivityLog> getLogsByTaskId(Long taskId, String userId) {
        // Fetch and return the activity logs for the task
        return activityLogRepository.findByTask(taskId, userId);
    }

    public List<ActivityLog> getActivityLogsByProjectId(Long projectId, String userId) {
        // Fetch and return the activity logs for the project
        return activityLogRepository.findByProjectId(projectId, userId);
    }
}
