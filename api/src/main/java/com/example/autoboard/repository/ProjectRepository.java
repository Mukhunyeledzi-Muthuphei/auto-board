package com.example.autoboard.repository;

import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.autoboard.entity.ProjectStatus;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByName(String name);

    List<Project> findByStatus(ProjectStatus status);

    List<Project> findByOwner(User owner);

    List<Project> findByNameContaining(String keyword);

    List<Project> findByDescriptionContaining(String keyword);
}
