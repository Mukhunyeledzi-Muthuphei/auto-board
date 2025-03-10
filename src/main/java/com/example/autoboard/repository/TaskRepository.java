package com.example.autoboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.autoboard.entity.Task;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByName(String title);
}