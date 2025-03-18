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

    public Optional<ProjectMember> getProjectMemberById(Long id, String userId) {
        Optional<ProjectMember> projectMember = projectMemberRepository.findById(id);
        if (projectMember.isPresent() && projectMember.get().getUser().getId().equals(userId)) {
            return projectMember;
        }
        return Optional.empty();
    }

    public List<ProjectMember> getProjectMembersByProject(Project project, String userId) {
        List<ProjectMember> projectMembers = projectMemberRepository.findByProject(project);
        return projectMembers.stream()
                .filter(pm -> pm.getUser().getId().equals(userId))
                .toList();
    }

    public List<ProjectMember> getProjectMemberByUser(User user, String userId) {
        // if (user.getId().equals(userId)) {
        return projectMemberRepository.findByUser(user);
        // }
        // return List.of();
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