package com.example.autoboard.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "project_status")
public class ProjectStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long project_status_id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public ProjectStatus() {
    }

    public ProjectStatus(String name) {
        this.name = name;
    }
    public Long getId() {
        return project_status_id;
    }
    public void setId(Long project_status_id) {
        this.project_status_id = project_status_id;
    }
    public String getName() {
        return name;
    }
    public void setStatus(String name) {
        this.name = name;
    }
}
