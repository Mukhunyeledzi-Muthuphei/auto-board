package com.example.autoboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.repository.query.Param;

import com.example.autoboard.entity.Task;
import com.example.autoboard.entity.User;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatusId(@Param("statusId") Long statusId);

    Optional<Task> findByIdAndAssignee(Long id, User assignee);

    List<Task> findByStatusIdAndAssignee(Long statusId, User assignee);
}