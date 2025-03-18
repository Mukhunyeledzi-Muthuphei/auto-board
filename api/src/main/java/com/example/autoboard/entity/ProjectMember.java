package com.example.autoboard.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "project_members")
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long project_member_id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public ProjectMember() {
    }

    public ProjectMember(Project project, User user) {
        this.project = project;
        this.user = user;
    }

    public Long getId() {
        return project_member_id;
    }

    public void setId(Long project_member_id) {
        this.project_member_id = project_member_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}