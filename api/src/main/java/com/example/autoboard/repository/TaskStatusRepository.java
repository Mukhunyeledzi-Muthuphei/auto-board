package com.example.autoboard.repository;

import com.example.autoboard.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    List<TaskStatus> findByName(String name);

    List<TaskStatus> findByNameContaining(String keyword);

}
