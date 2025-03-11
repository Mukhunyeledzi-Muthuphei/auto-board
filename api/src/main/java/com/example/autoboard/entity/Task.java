package com.example.autoboard.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks") // Specifies the table name
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    @Column(nullable = false, length = 100) // City name cannot be null
    private String title;

    // Default constructor
    public Task() {}

    // Constructor with parameters
    public Task(String title) {
        this.title = title;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
