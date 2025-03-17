package com.example.autoboard.service;

import com.example.autoboard.entity.ProjectStatus;
import com.example.autoboard.repository.ProjectStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectStatusService {

    private final ProjectStatusRepository projectStatusRepository;

    @Autowired
    public ProjectStatusService(ProjectStatusRepository projectStatusRepository) {
        this.projectStatusRepository = projectStatusRepository;
    }

    public List<ProjectStatus> getAllProjectStatuses() {
        return projectStatusRepository.findAll();
    }

    public Optional<ProjectStatus> getProjectStatusById(Long id) {
        return projectStatusRepository.findById(id);
    }

    public ProjectStatus getProjectStatusByName(String name) {
        return projectStatusRepository.findByName(name);
    }

}