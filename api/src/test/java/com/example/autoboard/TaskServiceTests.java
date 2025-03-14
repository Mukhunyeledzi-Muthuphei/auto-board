package com.example.autoboard;
import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.Task;
import com.example.autoboard.entity.TaskStatus;
import com.example.autoboard.entity.User;
import com.example.autoboard.repository.TaskRepository;
import com.example.autoboard.service.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {

      @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskStatus status;
    private Project project;
    private User assignee;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("This is a test task");
       
        status = new TaskStatus();
        status.setId(1L);
        status.setName("In Progress");

        project = new Project();
        project.setId(1L);
        project.setName("Project Alpha");

        assignee = new User();
        assignee.setId(1L);
        assignee.setFirstName("John");
        assignee.setLastName("Doe");

        task.setStatus(status);
        task.setProject(project);
        task.setAssignee(assignee);
    }

    @Test
    void testGetAllTasks() {
        // Arrange
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task));

        // Act
        List<Task> tasks = taskService.getAlltasks();

        // Assert
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("In Progress", tasks.get(0).getStatus().getName());
        assertEquals("Project Alpha", tasks.get(0).getProject().getName());
        assertEquals("John", tasks.get(0).getAssignee().getFirstName());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTaskById_Success() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        Task foundTask = taskService.getTaskById(1L);

        // Assert
        assertNotNull(foundTask);
        assertEquals("Test Task", foundTask.getTitle());
        assertEquals("In Progress", foundTask.getStatus().getName());
        assertEquals("Project Alpha", foundTask.getProject().getName());
        assertEquals("John", foundTask.getAssignee().getFirstName());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskById_NotFound() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> taskService.getTaskById(1L));
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTasksByStatus() {
        // Arrange
        when(taskRepository.findByStatusId(1L)).thenReturn(Arrays.asList(task));

        // Act
        List<Task> tasks = taskService.getTasksByStatus(1L);

        // Assert
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("In Progress", tasks.get(0).getStatus().getName());
        verify(taskRepository, times(1)).findByStatusId(1L);
    }

    @Test
    void testCreateTask() {
        // Arrange
        when(taskRepository.save(task)).thenReturn(task);

        // Act
        Task createdTask = taskService.createTask(task);

        // Assert
        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());
        assertEquals("In Progress", createdTask.getStatus().getName());
        assertEquals("Project Alpha", createdTask.getProject().getName());
        assertEquals("John", createdTask.getAssignee().getFirstName());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateTask_Success() {
        // Arrange
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("This is an updated task");
        updatedTask.setStatus(status);
        updatedTask.setProject(project);
        updatedTask.setAssignee(assignee);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        // Act
        Task result = taskService.updateTask(1L, updatedTask);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
        assertEquals("This is an updated task", result.getDescription());
        assertEquals("In Progress", result.getStatus().getName());
        assertEquals("Project Alpha", result.getProject().getName());
        assertEquals("John", result.getAssignee().getFirstName());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateTask_NotFound() {
        // Arrange
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> taskService.updateTask(1L, updatedTask));
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void testDeleteTask_Success() {
        // Arrange
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        // Act
        taskService.deleteTask(1L);

        // Assert
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTask_NotFound() {
        // Arrange
        when(taskRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> taskService.deleteTask(1L));
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, never()).deleteById(any());
    }

}
