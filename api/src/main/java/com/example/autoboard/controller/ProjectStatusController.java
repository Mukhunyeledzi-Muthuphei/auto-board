package com.example.autoboard.controller;

import com.example.autoboard.entity.ProjectStatus;
import com.example.autoboard.service.ProjectStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/project-status")
public class ProjectStatusController {

    private final ProjectStatusService projectStatusService;

    @Autowired
    public ProjectStatusController(ProjectStatusService projectStatusService) {
        this.projectStatusService = projectStatusService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectStatus>> getAllProjectStatuses() {
        List<ProjectStatus> projectStatuses = projectStatusService.getAllProjectStatuses();
        if (projectStatuses.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content if the list is empty
        }
        return ResponseEntity.ok(projectStatuses); // Return 200 OK with the list of project statuses
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectStatus> getProjectStatusById(@PathVariable Long id) {
        Optional<ProjectStatus> projectStatus = projectStatusService.getProjectStatusById(id);
        return projectStatus.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ProjectStatus> getProjectStatusByName(@PathVariable String name) {
        ProjectStatus projectStatus = projectStatusService.getProjectStatusByName(name);
        return projectStatus != null ? ResponseEntity.ok(projectStatus) : ResponseEntity.notFound().build();
    }

}