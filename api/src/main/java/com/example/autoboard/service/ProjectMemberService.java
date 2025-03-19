package com.example.autoboard.service;

import com.example.autoboard.entity.ProjectMember;
import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.User;
import com.example.autoboard.repository.ProjectMemberRepository;
import com.example.autoboard.repository.ProjectRepository; // Add this import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository; // Add ProjectRepository

    @Autowired
    public ProjectMemberService(ProjectMemberRepository projectMemberRepository, ProjectRepository projectRepository) {
        this.projectMemberRepository = projectMemberRepository;
        this.projectRepository = projectRepository; // Initialize ProjectRepository
    }

    public Optional<ProjectMember> getProjectMemberById(Long id, String userId) {
        Optional<ProjectMember> projectMember = projectMemberRepository.findById(id);
        if (projectMember.isPresent()) {
            ProjectMember pm = projectMember.get();
            // Fetch the project and validate the owner
            Optional<Project> projectOptional = projectRepository.findById(pm.getProject().getId());
            if (projectOptional.isPresent() && projectOptional.get().getOwner().getId().equals(userId)) {
                return projectMember;
            }
        }
        return Optional.empty();
    }

    public List<ProjectMember> getProjectMembersByProject(Project project, String userId) {
        // Fetch the project and validate the owner
        Optional<Project> projectOptional = projectRepository.findById(project.getId());
        if (projectOptional.isEmpty() || !projectOptional.get().getOwner().getId().equals(userId)) {
            throw new RuntimeException("User ID does not match project owner: " + userId);
        }

        return projectMemberRepository.findByProject(project);
    }

    public List<ProjectMember> getProjectMemberByUser(User user) {
        return projectMemberRepository.findByUser(user);
    }

    public ProjectMember createProjectMember(Project project, User user) {
        ProjectMember projectMember = new ProjectMember(project, user);
        return projectMemberRepository.save(projectMember);
    }

    public ProjectMember saveProjectMember(ProjectMember projectMember, String userId) {
        // Fetch the project and its owner using ProjectRepository
        Optional<Project> projectOptional = projectRepository.findById(projectMember.getProject().getId());
        if (projectOptional.isEmpty()) {
            throw new RuntimeException("Project not found for ID: " + projectMember.getProject().getId());
        }

        Project project = projectOptional.get();
        if (!project.getOwner().getId().equals(userId)) {
            throw new RuntimeException("User ID does not match: " + projectMember.getUser().getId() + " != " + userId);
        }

        return projectMemberRepository.save(projectMember);
    }

    public void deleteProjectMember(ProjectMember projectMember, String userId) {
        // Fetch the project from the ProjectMember
        Project project = projectMember.getProject();

        // Validate that the userId is the owner of the project
        Optional<Project> projectOptional = projectRepository.findById(project.getId());
        if (projectOptional.isEmpty()) {
            throw new RuntimeException("Project not found for ID: " + project.getId());
        }

        Project fetchedProject = projectOptional.get();
        if (!fetchedProject.getOwner().getId().equals(userId)) {
            throw new RuntimeException("User ID does not match project owner: " + userId);
        }

        // Fetch the ProjectMember using the project and user fields
        Optional<ProjectMember> projectMemberOptional = projectMemberRepository.findByProjectAndUser(
                projectMember.getProject(), projectMember.getUser());

        if (projectMemberOptional.isEmpty()) {
            throw new RuntimeException("Project member not found for the specified project and user.");
        }

        // Delete the ProjectMember
        projectMemberRepository.delete(projectMemberOptional.get());
        throw new RuntimeException("Project member deleted successfully.");
    }
}