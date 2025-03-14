// package com.example.autoboard.service;

// import com.example.autoboard.entity.ActivityLog;
// import com.example.autoboard.entity.Task;
// import com.example.autoboard.repository.ActivityLogRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Optional;

// @Service
// public class ActivityLogService {

// private final ActivityLogRepository activityLogRepository;

// @Autowired
// public ActivityLogService(ActivityLogRepository activityLogRepository) {
// this.activityLogRepository = activityLogRepository;
// }

// public List<ActivityLog> getAllLogs() {
// return activityLogRepository.findAll();
// }

// public Optional<ActivityLog> getLogById(Long id) {
// return activityLogRepository.findById(id);
// }

// public List<ActivityLog> getLogsByTaskId(Long taskId) {
// return activityLogRepository.findByTaskId(taskId);
// }

// public void createLog(Task task, String action) {
// ActivityLog log = new ActivityLog();
// log.setTask(task);
// log.setAction(action);
// log.setTimestamp(LocalDateTime.now());
// activityLogRepository.save(log);
// }
// }

// // TODO add logic for creating logs in services
// //private final ActivityLogService activityLogService;
// //
// //// Log activity using Task reference
// //activityLogService.createLog(savedTask, "Created task");
