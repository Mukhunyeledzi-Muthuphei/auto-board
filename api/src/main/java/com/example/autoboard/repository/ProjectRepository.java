package com.example.autoboard.repository;

import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.autoboard.entity.ProjectStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByName(String name);

    List<Project> findByStatus(ProjectStatus status);

    List<Project> findByOwner(User owner);

    List<Project> findByNameContaining(String keyword);

    List<Project> findByDescriptionContaining(String keyword);

    @Query("""
        SELECT p FROM Project p
        LEFT JOIN ProjectMember pm ON p = pm.project
        WHERE p.owner.id = :userId OR pm.user.id = :userId
    """)
    List<Project> findAllProjectsByUser(@Param("userId") String userId);

    @Query(value = """
        SELECT DISTINCT p.* FROM projects p
        LEFT JOIN project_members pm ON p.project_id = pm.project_id
        WHERE p.project_id = :projectId
        AND (p.owner_id = :userId OR pm.user_id = :userId)
    """, nativeQuery = true)
    Optional<Project> findProjectByIdAndUser(@Param("projectId") Long projectId, @Param("userId") String userId);
}
