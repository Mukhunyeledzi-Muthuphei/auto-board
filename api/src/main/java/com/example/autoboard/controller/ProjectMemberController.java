package com.example.autoboard.controller;

import com.example.autoboard.entity.ProjectMember;
import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.User;
import com.example.autoboard.service.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

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
        String userId = TokenHelper.extractUserIdFromToken(token);
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Unauthorized
        }
        Optional<ProjectMember> projectMember = projectMemberService.getProjectMemberById(id, userId);
        return projectMember.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Not Found
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ProjectMember>> getProjectMembersByProject(@PathVariable Long projectId,
            @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of()); // Unauthorized
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        Project project = new Project();
        project.setId(projectId);
        List<ProjectMember> members = projectMemberService.getProjectMembersByProject(project, userId);
        if (members.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of()); // Not Found
        }
        return ResponseEntity.ok(members); // Success
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProjectMember>> getProjectMembersByUser(@PathVariable String userId,
            @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of()); // Unauthorized
        }
        String userIdFromToken = TokenHelper.extractUserIdFromToken(token);
        if (!userId.equals(userIdFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(List.of()); // Forbidden
        }
        User user = new User();
        user.setId(userId);
        List<ProjectMember> members = projectMemberService.getProjectMemberByUser(user);
        if (members.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of()); // Not Found
        }
        return ResponseEntity.ok(members); // Success
    }

    @PostMapping
    public ResponseEntity<ProjectMember> createProjectMember(@RequestBody ProjectMember projectMember,
            @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Unauthorized
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        ProjectMember savedMember = projectMemberService.saveProjectMember(projectMember, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMember); // Created
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProjectMember(@RequestBody ProjectMember projectMember,
            @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Unauthorized
        }
        String userId = TokenHelper.extractUserIdFromToken(token);
        projectMemberService.deleteProjectMember(projectMember, userId);
        return ResponseEntity.ok().build(); // Success
    }
}