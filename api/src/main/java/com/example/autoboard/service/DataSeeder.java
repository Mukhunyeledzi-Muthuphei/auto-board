package com.example.autoboard.service;

import com.example.autoboard.entity.ProjectStatus;
import com.example.autoboard.entity.TaskStatus;
import com.example.autoboard.repository.ProjectStatusRepository;
import com.example.autoboard.repository.TaskStatusRepository;
import org.springframework.stereotype.Service;

@Service
public class DataSeeder {

    private final ProjectStatusRepository projectStatusRepository;
    private final TaskStatusRepository taskStatusRepository;

    public DataSeeder(ProjectStatusRepository projectStatusRepository, TaskStatusRepository taskStatusRepository) {
        this.projectStatusRepository = projectStatusRepository;
        this.taskStatusRepository = taskStatusRepository;
    }

    public void seedDatabase() {
        // Prefill project_status table
        if (projectStatusRepository.count() == 0) {
            projectStatusRepository.save(new ProjectStatus("Planning"));
            projectStatusRepository.save(new ProjectStatus("In Progress"));
            projectStatusRepository.save(new ProjectStatus("Completed"));
            projectStatusRepository.save(new ProjectStatus("On Hold"));
        }

        // Prefill task_status table
        if (taskStatusRepository.count() == 0) {
            taskStatusRepository.save(new TaskStatus("To Do"));
            taskStatusRepository.save(new TaskStatus("In Progress"));
            taskStatusRepository.save(new TaskStatus("Review"));
            taskStatusRepository.save(new TaskStatus("Done"));
        }
    }
}
