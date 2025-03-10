package com.example.autoboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.autoboard.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> { }