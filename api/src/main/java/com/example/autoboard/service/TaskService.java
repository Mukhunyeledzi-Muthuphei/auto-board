package com.example.autoboard.service;

import com.example.autoboard.entity.Task;
import com.example.autoboard.entity.User;
import com.example.autoboard.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private final TaskRepository taskRepository;

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

    public Task createTask(Task task, User assignee) {
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

}
