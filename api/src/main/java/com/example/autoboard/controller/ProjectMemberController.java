package com.example.autoboard.controller;

import com.example.autoboard.entity.ProjectMember;
import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.User;
import com.example.autoboard.service.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Optional;
import com.example.autoboard.helpers.TokenHelper;

@RestController
@RequestMapping("/api/project-members")
public class ProjectMemberController {

    @Value("${google.client.id}")
    private String clientId;

    private final ProjectMemberService projectMemberService;

    @Autowired
    public ProjectMemberController(ProjectMemberService projectMemberService) {
        this.projectMemberService = projectMemberService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectMember> getProjectMemberById(@PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        String userId = extractUserIdFromToken(token);
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.notFound().build();
        }
        Optional<ProjectMember> projectMember = projectMemberService.getProjectMemberById(id, userId);
        return projectMember.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/project/{projectId}")
    public List<ProjectMember> getProjectMembersByProject(@PathVariable Long projectId,
            @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return List.of();
        }
        String userId = extractUserIdFromToken(token);
        Project project = new Project();
        project.setId(projectId);
        return projectMemberService.getProjectMembersByProject(project, userId);
    }

    @GetMapping("/user/{userId}")
    public List<ProjectMember> getProjectMembersByUser(@PathVariable String userId,
            @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return List.of();
        }
        String userIdFromToken = extractUserIdFromToken(token);
        if (!userId.equals(userIdFromToken)) {
            return List.of(); // Return an empty list if the user ID does not match the token
        }
        User user = new User();
        user.setId(userId);
        return projectMemberService.getProjectMembersByUser(user, userId);
    }

    @PostMapping
    public ProjectMember createProjectMember(@RequestBody ProjectMember projectMember,
            @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return null;
        }
        String userId = extractUserIdFromToken(token);
        projectMember.setUser(new User(userId));
        return projectMemberService.saveProjectMember(projectMember);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectMember> updateProjectMember(@PathVariable Long id,
            @RequestBody ProjectMember projectMember, @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.notFound().build();
        }
        String userId = extractUserIdFromToken(token);
        Optional<ProjectMember> existingProjectMember = projectMemberService.getProjectMemberById(id, userId);
        if (existingProjectMember.isPresent()) {
            projectMember.setId(id);
            return ResponseEntity.ok(projectMemberService.saveProjectMember(projectMember));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectMember(@PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.notFound().build();
        }
        String userId = extractUserIdFromToken(token);
        if (projectMemberService.getProjectMemberById(id, userId).isPresent()) {
            projectMemberService.deleteProjectMember(id, userId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private String extractUserIdFromToken(String token) {
        // Implement the logic to extract the user ID from the Google JWT token
        // This is a placeholder implementation
        return token.substring(7); // Assuming the token is in the format "Bearer <token>"
    }
}