package com.example.autoboard.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "task_status")
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long task_status_id;

    @Column(name = "name", nullable = false)
    private String name;

    public Long getId() { return task_status_id; }
    public void setId(Long task_status_id) { this.task_status_id = task_status_id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}