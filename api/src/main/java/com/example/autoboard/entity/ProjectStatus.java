package com.example.autoboard.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "project_status")
public class ProjectStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_status_id")
    private Long projectStatusId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public ProjectStatus() {
    }

    public ProjectStatus(String name) {
        this.name = name;
    }

    public Long getId() {
        return projectStatusId;
    }

    public void setId(Long projectStatusId) {
        this.projectStatusId = projectStatusId;
    }

    public String getName() {
        return name;
    }

    public void setStatus(String name) {
        this.name = name;
    }
}
