package com.example.autoboard.service;

import com.example.autoboard.entity.ProjectStatus;
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

    public Optional<TaskStatus> getTaskStatusById(Long id) {
        return taskStatusRepository.findById(id);
    }

    public TaskStatus getTaskStatusByName(String name) {
        return taskStatusRepository.findByName(name);
    }
}
