package com.example.autoboard.repository;

import com.example.autoboard.entity.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectStatusRepository extends JpaRepository<ProjectStatus, Long> {
    ProjectStatus findByName(String name);
}