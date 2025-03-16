package com.example.autoboard.controller;

import com.example.autoboard.entity.ProjectMember;
import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.User;
import com.example.autoboard.service.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/project-members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @Autowired
    public ProjectMemberController(ProjectMemberService projectMemberService) {
        this.projectMemberService = projectMemberService;
    }

    // TODO: Should we have an endpoint to get all project members?
    @GetMapping
    public List<ProjectMember> getAllProjectMembers() {
        return projectMemberService.getAllProjectMembers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectMember> getProjectMemberById(@PathVariable Long id) {
        Optional<ProjectMember> projectMember = projectMemberService.getProjectMemberById(id);
        return projectMember.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/project/{projectId}")
    public List<ProjectMember> getProjectMembersByProject(@PathVariable Long projectId) {
        Project project = new Project();
        project.setId(projectId);
        return projectMemberService.getProjectMembersByProject(project);
    }

    @GetMapping("/user/{userId}")
    public List<ProjectMember> getProjectMembersByUser(@PathVariable String userId) {
        User user = new User();
        user.setId(userId);
        return projectMemberService.getProjectMembersByUser(user);
    }

    @PostMapping
    public ProjectMember createProjectMember(@RequestBody ProjectMember projectMember) {
        return projectMemberService.saveProjectMember(projectMember);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectMember> updateProjectMember(@PathVariable Long id,
            @RequestBody ProjectMember projectMember) {
        Optional<ProjectMember> existingProjectMember = projectMemberService.getProjectMemberById(id);
        if (existingProjectMember.isPresent()) {
            projectMember.setId(id);
            return ResponseEntity.ok(projectMemberService.saveProjectMember(projectMember));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectMember(@PathVariable Long id) {
        if (projectMemberService.getProjectMemberById(id).isPresent()) {
            projectMemberService.deleteProjectMember(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}