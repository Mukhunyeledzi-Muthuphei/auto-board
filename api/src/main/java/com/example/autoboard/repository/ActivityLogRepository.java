package com.example.autoboard.repository;

import com.example.autoboard.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.autoboard.entity.Task;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findByTask(Task task);

    @Query("SELECT al FROM ActivityLog al WHERE al.task.project.project_id = :projectId")
    List<ActivityLog> findByProjectId(@Param("projectId") Long projectId);
}