package com.example.autoboard.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "project_members", uniqueConstraints = @UniqueConstraint(columnNames = { "project_id", "user_id" })

)

public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectMemberId;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false, referencedColumnName = "project_id")
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
        return projectMemberId;
    }

    public void setId(Long projectMemberId) {
        this.projectMemberId = projectMemberId;
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