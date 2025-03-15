package com.example.autoboard.service;

import com.example.autoboard.entity.TaskStatus;
import com.example.autoboard.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskStatusService {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    public List<TaskStatus> getAllTaskStatuses() {
        return taskStatusRepository.findAll();
    }

    public TaskStatus getTaskStatusById(Long id) {
        Optional<TaskStatus> taskStatus = taskStatusRepository.findById(id);
        return taskStatus.orElse(null);
    }

    public TaskStatus createTaskStatus(TaskStatus taskStatus) {
        return taskStatusRepository.save(taskStatus);
    }

    public TaskStatus updateProject(Long id, TaskStatus taskStatusDetails) {
        Optional<TaskStatus> taskStatusOptional = taskStatusRepository.findById(id);
        if (taskStatusOptional.isPresent()) {
            TaskStatus taskStatus = taskStatusOptional.get();
            taskStatus.setName(taskStatusDetails.getName());
            // Update other fields as necessary
            return taskStatusRepository.save(taskStatus);
        } else {
            return null;
        }
    }

    public boolean deleteTaskStatus(Long id) {
        if (taskStatusRepository.existsById(id)) {
            taskStatusRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
