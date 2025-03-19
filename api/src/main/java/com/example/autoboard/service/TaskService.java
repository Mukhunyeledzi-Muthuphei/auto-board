package com.example.autoboard.service;

import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.Task;
import com.example.autoboard.entity.TaskStatus;
import com.example.autoboard.entity.User;
import com.example.autoboard.repository.ProjectRepository;
import com.example.autoboard.repository.TaskRepository;
import com.example.autoboard.repository.TaskStatusRepository;
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

    @Autowired ProjectRepository projectRepository;

    @Autowired TaskStatusRepository taskStatusRepository;

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

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task task, String assignee) {
       Optional<Task> taskOptional = taskRepository.findById(id);
    if (taskOptional.isPresent()) {
        Task tasks = taskOptional.get();
        if (!tasks.getAssignee().getId().equals(assignee)) {
            return null;
        }
        else{
            tasks.setTitle(task.getTitle());
            tasks.setDescription(task.getDescription());
            tasks.setStatus(task.getStatus());
            tasks.setProject(task.getProject());
        }

        Task savedTask = taskRepository.save(tasks);

        return taskRepository.save(savedTask);
    } else {
        return null;
    }
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
