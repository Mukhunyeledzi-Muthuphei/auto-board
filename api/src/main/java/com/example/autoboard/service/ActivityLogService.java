package com.example.autoboard.service;

import com.example.autoboard.entity.ActivityLog;
import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.ProjectMember;
import com.example.autoboard.entity.Task;
import com.example.autoboard.entity.User;
import com.example.autoboard.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.autoboard.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final ProjectMemberService projectMemberService;
    private final TaskRepository taskRepository;

    @Autowired
    public ActivityLogService(ActivityLogRepository activityLogRepository, ProjectMemberService projectMemberService,
            TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.activityLogRepository = activityLogRepository;
        this.projectMemberService = projectMemberService;
    }

    public List<ActivityLog> getAllLogs(String userId) {
        List<ProjectMember> userProjects = projectMemberService.getProjectMemberByUser(new User(userId), userId);
        return activityLogRepository.findAll().stream()
                .filter(log -> userProjects.stream()
                        .anyMatch(member -> member.getProject().getId().equals(log.getTask().getProject().getId())))
                .collect(Collectors.toList());
    }

    public Optional<ActivityLog> getLogById(Long id, String userId) {
        Optional<ActivityLog> log = activityLogRepository.findById(id);
        if (log.isPresent()) {
            Project project = log.get().getTask().getProject();
            List<ProjectMember> userProjects = projectMemberService.getProjectMemberByUser(new User(userId), userId);
            boolean isMember = userProjects.stream()
                    .anyMatch(member -> member.getProject().getId().equals(project.getId()));
            if (isMember) {
                return log;
            }
        }
        return Optional.empty();
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
