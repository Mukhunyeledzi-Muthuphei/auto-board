// package com.example.autoboard.repository;

// import java.util.List;

// import org.springframework.data.jpa.repository.JpaRepository;

// import org.springframework.data.repository.query.Param;

// import com.example.autoboard.entity.Task;

// public interface TaskRepository extends JpaRepository<Task, Long> {

// List<Task> findByStatusId(@Param("statusId") long statusId);
// }