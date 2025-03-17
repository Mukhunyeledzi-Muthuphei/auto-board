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

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAlltasks(String userId) {
        return taskRepository.findAllByUserId(userId);
    }

    public Task getTaskById(Long id, String userId) {
        Optional<Task> taskOptional = taskRepository.findByIdAndUserId(id, userId);
        return taskOptional.orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    public List<Task> getTasksByStatus(Long statusId, String userId) {
        return taskRepository.findByStatusIdAndUserId(statusId, userId);
    }

    public Task createTask(Task task, String userId) {
        task.setAssignee(userId);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task task, String userId) {
        Task existingTask = getTaskById(id, userId);
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setProject(task.getProject());
        existingTask.setAssignee(task.getAssignee());
        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id, String userId) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        else{
        taskRepository.deleteById(id);
        }
        
    }

}
