package com.example.autoboard.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long project_id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    // TODO: Might need to come back to this when the status table exists
    @Column(name = "status_id", nullable = false)
    private Long statusId;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id", nullable = false)
    private User owner;

    public Project() {
    }

    public Project(String name, String description, Long statusId, User owner) {
        this.name = name;
        this.description = description;
        this.statusId = statusId;
        this.owner = owner;
    }

    public Long getId() {
        return project_id;
    }

    public void setId(Long project_id) {
        this.project_id = project_id;
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
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
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

        if (project_id != null ? !project_id.equals(project.project_id) : project.project_id != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(project_id);
    }
}
