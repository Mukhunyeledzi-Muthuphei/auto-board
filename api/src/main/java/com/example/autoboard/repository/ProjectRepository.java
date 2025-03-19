package com.example.autoboard.repository;

import com.example.autoboard.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /*
     * INFO
     * @Query annotation allows you to define custom JPQL or native SQL queries
     * nativeQuery specifies query written in SQL, not JPQL
     */
    
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
