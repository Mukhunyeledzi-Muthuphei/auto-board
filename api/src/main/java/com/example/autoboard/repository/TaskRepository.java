package com.example.autoboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.autoboard.entity.Task;
import com.example.autoboard.entity.User;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatusId(@Param("statusId") Long statusId);

    Optional<Task> findByIdAndAssignee(Long id, User assignee);

    List<Task> findByStatusIdAndAssignee(Long statusId, User assignee);

    @Query("SELECT t " +
            "FROM Task t " +
            "WHERE t.assignee.user_id = :userId")
    List<Task> findTasksByAssigneeId(@Param("userId") String userId);

    @Query(value = "SELECT DISTINCT t.* " +
            "FROM tasks t " +
            "JOIN projects p ON t.project_id = p.project_id " +
            "LEFT JOIN project_members pm ON t.project_id = pm.project_id " +
            "WHERE pm.user_id = :userId " +
            "OR p.owner_id = :userId", nativeQuery = true)
    List<Task> findTasksAvailableToUser(@Param("userId") String userId);

    @Query(value = "SELECT t.* " +
            "FROM tasks t " +
            "JOIN projects p ON t.project_id = p.project_id " +
            "LEFT JOIN project_members pm ON t.project_id = pm.project_id " +
            "WHERE t.task_id = :taskId " +
            "AND (p.owner_id = :userId OR pm.user_id = :userId)", nativeQuery = true)
    Optional<Task> findTaskByIdAndUserAccess(@Param("taskId") Long taskId, @Param("userId") String userId);

    @Query(value = "SELECT DISTINCT t.* " +
            "FROM tasks t " +
            "JOIN projects p ON t.project_id = p.project_id " +
            "LEFT JOIN project_members pm ON t.project_id = pm.project_id " +
            "WHERE t.project_id = :projectId " +
            "AND (p.owner_id = :userId OR pm.user_id = :userId)", nativeQuery = true)
    List<Task> findAllTasksByProjectIdAndUserAccess(@Param("projectId") Long projectId, @Param("userId") String userId);
}