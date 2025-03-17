package com.example.autoboard.controller;

import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.ProjectMember;
import com.example.autoboard.service.ProjectMemberService;
import com.example.autoboard.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import com.example.autoboard.helpers.TokenHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

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
    public ResponseEntity<List<Project>> getAllProjects(
            @RequestHeader("Authorization") String token
    ) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = TokenHelper.extractUserIdFromToken(token);
        List<Project> projects = projectService.getAllProjects();

        List<Project> allowedProjects = projects.stream()
                .filter(project ->
                        project.getOwner().getId().equals(userId) ||
                                !projectMemberService.getProjectMembersByProject(project, userId).isEmpty()
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok(allowedProjects);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable String projectId,
                                                  @RequestHeader("Authorization") String token) {

        Long id =  Long.parseLong(projectId);
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = TokenHelper.extractUserIdFromToken(token);
        Project project = projectService.getProjectById(id);

        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Check if the user is the project owner or a member
        List<ProjectMember> projectMembers = projectMemberService.getProjectMembersByProject(project, userId);
        if (!project.getOwner().getId().equals(userId) && projectMembers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(project);
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
