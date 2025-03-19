package com.example.autoboard.service;

import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.Task;
import com.example.autoboard.entity.User;
import com.example.autoboard.repository.TaskRepository;
import com.example.autoboard.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private final TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired ProjectService projectService;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAlltasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id, User assignee) {
        Optional<Task> taskOptional = taskRepository.findByIdAndAssignee(id, assignee);
        return taskOptional.orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    public List<Task> getTasksByStatus(Long statusId, User assignee) {
        return taskRepository.findByStatusIdAndAssignee(statusId, assignee);
    }

    public Task createTask(Task task, User assignee, Long projectId) {
        System.out.println("========================projectId================");
        System.out.println(projectId);
        System.out.println(assignee.getId());
        System.out.println("Received Task Status: " + task.getStatus());
        System.out.println("========================projectId================");
        String userId = assignee.getId();
        Project project = projectService.getProjectByIdForUser(projectId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found or not accessible"));
        task.setProject(project);  // Ensure Task references a managed Project entity
        task.setAssignee(assignee);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task task, User assignee) {
        Task existingTask = getTaskById(id, assignee);
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setProject(task.getProject());
        existingTask.setAssignee(task.getAssignee());
        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id, String assignee) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        else{
        taskRepository.deleteById(id);
        }
        
    }

    public Task assignTask(Long taskId, String assigneeId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            return null; 
        }
        User assignee = userRepository.findById(assigneeId).orElse(null);
        if (assignee == null) {
            return null;
        }
        task.setAssignee(assignee);
        return taskRepository.save(task);
    }

    // Get all tasks from all projects available to user
    public List<Task> getTasksAvailableToUser(String userId) {
        return taskRepository.findTasksAvailableToUser(userId);
    }

    // For fetching tasks assigned to a user
    public List<Task> getTasksByUserId(String userId) {
        return taskRepository.findTasksByAssigneeId(userId);
    }

    // For fetching a task by ID if user has access
    public Optional<Task> getTasksById(Long taskId, String userId) {
        return taskRepository.findTaskByIdAndUserAccess(taskId, userId);
    }

}
