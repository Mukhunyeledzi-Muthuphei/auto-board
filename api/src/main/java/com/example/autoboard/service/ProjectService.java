package com.example.autoboard.service;

import com.example.autoboard.entity.Project;
import com.example.autoboard.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getAllProjectsByUserId(String id) {
        return projectRepository.findAllProjectsByUser(id);
    }

    public Optional<Project> getProjectByIdForUser(Long projectId, String userId) {
        return projectRepository.findProjectByIdAndUser(projectId, userId);
    }

    public Project getProjectById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.orElse(null);
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project projectDetails, String userId) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            if (!project.getOwner().getId().equals(userId)) {
                return null;
            }
            project.setName(projectDetails.getName());
            project.setDescription(projectDetails.getDescription());
            project.setStatus(projectDetails.getStatus());
            // Update other fields as necessary
            return projectRepository.save(project);
        } else {
            return null;
        }
    }

    public boolean deleteProject(Long id, String userId) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            if (project.getOwner().getId().equals(userId)) {
                projectRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }
}
