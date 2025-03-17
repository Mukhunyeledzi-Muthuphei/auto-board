package com.example.autoboard.service;

import com.example.autoboard.entity.ActivityLog;
import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.ProjectMember;
import com.example.autoboard.entity.Task;
import com.example.autoboard.entity.User;
import com.example.autoboard.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final ProjectMemberService projectMemberService;

    @Autowired
    public ActivityLogService(ActivityLogRepository activityLogRepository, ProjectMemberService projectMemberService) {
        this.activityLogRepository = activityLogRepository;
        this.projectMemberService = projectMemberService;
    }

    public List<ActivityLog> getAllLogs(String userId) {
        List<ProjectMember> userProjects = projectMemberService.getProjectMembersByUser(new User(userId), userId);
        return activityLogRepository.findAll().stream()
                .filter(log -> userProjects.stream()
                        .anyMatch(member -> member.getProject().getId().equals(log.getTask().getProject().getId())))
                .collect(Collectors.toList());
    }

    public Optional<ActivityLog> getLogById(Long id, String userId) {
        Optional<ActivityLog> log = activityLogRepository.findById(id);
        if (log.isPresent()) {
            Project project = log.get().getTask().getProject();
            List<ProjectMember> userProjects = projectMemberService.getProjectMembersByUser(new User(userId), userId);
            boolean isMember = userProjects.stream()
                    .anyMatch(member -> member.getProject().getId().equals(project.getId()));
            if (isMember) {
                return log;
            }
        }
        return Optional.empty();
    }

    public List<ActivityLog> getLogsByTaskId(Long taskId, String userId) {
        List<ProjectMember> userProjects = projectMemberService.getProjectMembersByUser(new User(userId), userId);
        return activityLogRepository.findByTaskId(taskId).stream()
                .filter(log -> userProjects.stream()
                        .anyMatch(member -> member.getProject().getId().equals(log.getTask().getProject().getId())))
                .collect(Collectors.toList());
    }

    public void createLog(Task task, String action) {
        ActivityLog log = new ActivityLog();
        log.setTask(task);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());
        activityLogRepository.save(log);
    }
}
