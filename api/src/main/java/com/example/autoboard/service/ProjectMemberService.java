package com.example.autoboard.service;

import com.example.autoboard.entity.ProjectMember;
import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.User;
import com.example.autoboard.repository.ProjectMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;

    @Autowired
    public ProjectMemberService(ProjectMemberRepository projectMemberRepository) {
        this.projectMemberRepository = projectMemberRepository;
    }

    public List<ProjectMember> getAllProjectMembers() {
        return projectMemberRepository.findAll();
    }

    public Optional<ProjectMember> getProjectMemberById(Long id) {
        return projectMemberRepository.findById(id);
    }

    public List<ProjectMember> getProjectMembersByProject(Project project) {
        return projectMemberRepository.findByProject(project);
    }

    public List<ProjectMember> getProjectMembersByUser(User user) {
        return projectMemberRepository.findByUser(user);
    }

    public ProjectMember saveProjectMember(ProjectMember projectMember) {
        return projectMemberRepository.save(projectMember);
    }

    public void deleteProjectMember(Long id, String userId) {
        Optional<ProjectMember> projectMemberOptional = projectMemberRepository.findById(id);
        if (projectMemberOptional.isPresent()) {
            ProjectMember projectMember = projectMemberOptional.get();
            Project project = projectMember.getProject();
            if (projectMember.getUser().getId().equals(userId) || project.getOwner().getId().equals(userId)) {
                projectMemberRepository.deleteById(id);
            }
        }
    }
}