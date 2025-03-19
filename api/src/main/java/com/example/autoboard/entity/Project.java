package com.example.autoboard.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "project_status_id", nullable = false)
    private ProjectStatus status;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id", nullable = false)
    private User owner;

    public Project() {
    }

    public Project(String name, String description, ProjectStatus status, User owner) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.owner = owner;
    }

    public Long getId() {
        return projectId;
    }

    public void setId(Long projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStatusId() {
        return this.status.getId();
    }

    public void setStatusId(Long statusId) {
        this.status.setId(statusId);
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Project project = (Project) o;

        return projectId != null ? projectId.equals(project.projectId) : project.projectId == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId);
    }
}
