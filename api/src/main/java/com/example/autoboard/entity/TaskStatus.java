package com.example.autoboard.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "task_status")
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_status_id")
    private Long taskStatusId;

    @Column(name = "name", nullable = false)
    private String name;

    public TaskStatus() {
    }

    public TaskStatus(String name) {
        this.name = name;
    }

    public Long getId() {
        return taskStatusId;
    }

    public void setId(Long taskStatusId) {
        this.taskStatusId = taskStatusId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}