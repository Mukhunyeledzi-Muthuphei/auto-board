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

    @Query("SELECT al FROM ActivityLog al " +
            "JOIN al.task t " +
            "JOIN t.project p " +
            "LEFT JOIN ProjectMember pm ON t.project = pm.project " +
            "WHERE t.id = :taskId " +
            "AND (p.owner.id = :userId OR pm.user.id = :userId)")
    List<ActivityLog> findByTask(@Param("taskId") Long taskId, @Param("userId") String userId);

    @Query(value = "SELECT al.* " +
            "FROM activity_logs al " +
            "JOIN tasks t ON al.task_id = t.task_id " +
            "JOIN projects p ON t.project_id = p.project_id " +
            "LEFT JOIN project_members pm ON t.project_id = pm.project_id " +
            "WHERE p.project_id = :projectId " +
            "AND (p.owner_id = :userId OR pm.user_id = :userId)", nativeQuery = true)
    List<ActivityLog> findByProjectId(@Param("projectId") Long projectId, @Param("userId") String userId);
}