// package com.example.autoboard.service;

// import com.example.autoboard.entity.Task;
// import com.example.autoboard.repository.TaskRepository;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// @Service
// public class TaskService {

// private final TaskRepository taskRepository;

// @Autowired
// public TaskService(TaskRepository taskRepository) {
// this.taskRepository = taskRepository;
// }

// public List<Task> getAlltasks() {
// return taskRepository.findAll();
// }

// public Task getTaskById(Long id) {
// Optional<Task> taskOptional = taskRepository.findById(id);
// return taskOptional.orElseThrow(() -> new RuntimeException("Task not found
// with id: " + id));
// }

// public List<Task> getTasksByStatus(Long statusId) {
// return taskRepository.findByStatusId(statusId);
// }

// public Task createTask(Task task) {
// return taskRepository.save(task);
// }

// public Task updateTask(Long id, Task task) {
// Task existingTask = getTaskById(id);
// existingTask.setTitle(task.getTitle());
// existingTask.setDescription(task.getDescription());
// existingTask.setStatus(task.getStatus());
// existingTask.setProject(task.getProject());
// existingTask.setAssignee(task.getAssignee());
// return taskRepository.save(existingTask);
// }

// public void deleteTask(Long id) {
// if (!taskRepository.existsById(id)) {
// throw new RuntimeException("Task not found with id: " + id);
// }
// taskRepository.deleteById(id);
// }

// }
