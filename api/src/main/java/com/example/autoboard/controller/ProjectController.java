package com.example.autoboard.controller;

import com.example.autoboard.entity.Project;
import com.example.autoboard.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.autoboard.helpers.TokenHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import com.example.autoboard.service.ProjectMemberService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Value("${google.client.id}")
    private String clientId;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project,
            @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        if (!project.getOwner().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Project createdProject = projectService.createProject(project);
        projectMemberService.createProjectMember(createdProject, createdProject.getOwner());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project projectDetails,
            @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.notFound().build();
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        Project updatedProject = projectService.updateProject(id, projectDetails, userId);
        if (updatedProject != null) {
            return ResponseEntity.ok(updatedProject);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.notFound().build();
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        boolean isDeleted = projectService.deleteProject(id, userId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
