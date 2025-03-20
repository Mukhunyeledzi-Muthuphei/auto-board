package com.example.autoboard.service;

import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.Task;
import com.example.autoboard.entity.TaskStatus;
import com.example.autoboard.entity.User;
import com.example.autoboard.repository.TaskRepository;
import com.example.autoboard.repository.UserRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private User user;
    private Project project;
    private TaskStatus status;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId("user1");
        user.setFirstName("John");
        user.setLastName("Doe");

        project = new Project();
        project.setId(1L);
        project.setName("New Project");

        status = new TaskStatus();
        status.setId(1L);
        status.setName("In Progress");

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(status);
        task.setAssignee(user);
        task.setProject(project);
    }

    @Test
    void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task));

        List<Task> tasks = taskService.getAlltasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("In Progress", tasks.get(0).getStatus().getName());
        assertEquals("New Project", tasks.get(0).getProject().getName());
        assertEquals("John", tasks.get(0).getAssignee().getFirstName());
        assertEquals("Doe", tasks.get(0).getAssignee().getLastName());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTaskById() {
        when(taskRepository.findByIdAndAssignee(1L, user)).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTaskById(1L, user);

        assertNotNull(foundTask);
        assertEquals("Test Task", foundTask.getTitle());
        assertEquals("In Progress", foundTask.getStatus().getName());
        assertEquals("New Project", foundTask.getProject().getName());
        assertEquals("John", foundTask.getAssignee().getFirstName());
        assertEquals("Doe", foundTask.getAssignee().getLastName());
        verify(taskRepository, times(1)).findByIdAndAssignee(1L, user);
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(taskRepository.findByIdAndAssignee(1L, user)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.getTaskById(1L, user));
        verify(taskRepository, times(1)).findByIdAndAssignee(1L, user);
    }

    @Test
    void testCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(task);

        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());
        assertEquals("In Progress", createdTask.getStatus().getName());
        assertEquals("New Project", createdTask.getProject().getName());
        assertEquals("John", createdTask.getAssignee().getFirstName());
        assertEquals("Doe", createdTask.getAssignee().getLastName());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateTask() {
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus(status);
        updatedTask.setProject(project);
        updatedTask.setAssignee(user);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.updateTask(1L, updatedTask, "user1");

        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals("In Progress", result.getStatus().getName());
        assertEquals("New Project", result.getProject().getName());
        assertEquals("John", result.getAssignee().getFirstName());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Task result = taskService.updateTask(1L, task, "user1");

        assertNull(result);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteTaskSuccess() {
        // Arrange
        Long taskId = 1L;
        String userId = "user-123";
        Task task = new Task();
        task.setId(taskId);

        // Mock the repository to return a task when findTaskByIdAndUserAccess is called
        when(taskRepository.findTaskByIdAndUserAccess(taskId, userId)).thenReturn(Optional.of(task));

        // Act
        boolean result = taskService.deleteTask(taskId, userId);

        // Assert
        assertTrue(result);
        verify(taskRepository, times(1)).findTaskByIdAndUserAccess(taskId, userId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void testDeleteTaskFailureTaskNotFound() {
        // Arrange
        Long taskId = 1L;
        String userId = "user-123";

        // Mock the repository to return an empty optional (task not found or user does
        // not have access)
        when(taskRepository.findTaskByIdAndUserAccess(taskId, userId)).thenReturn(Optional.empty());

        // Act
        boolean result = taskService.deleteTask(taskId, userId);

        // Assert
        assertFalse(result);
        verify(taskRepository, times(1)).findTaskByIdAndUserAccess(taskId, userId);
        verify(taskRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteTaskFailureUserDoesNotHaveAccess() {
        // Arrange
        Long taskId = 1L;
        String userId = "user-123";

        // Mock the repository to return an empty optional (user does not have access)
        when(taskRepository.findTaskByIdAndUserAccess(taskId, userId)).thenReturn(Optional.empty());

        // Act
        boolean result = taskService.deleteTask(taskId, userId);

        // Assert
        assertFalse(result);
        verify(taskRepository, times(1)).findTaskByIdAndUserAccess(taskId, userId);
        verify(taskRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetTasksAvailableToUser() {
        when(taskRepository.findTasksAvailableToUser("user1")).thenReturn(Arrays.asList(task));

        List<Task> tasks = taskService.getTasksAvailableToUser("user1");

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        verify(taskRepository, times(1)).findTasksAvailableToUser("user1");
    }

    @Test
    void testGetTasksByUserId() {
        when(taskRepository.findTasksByAssigneeId("user1")).thenReturn(Arrays.asList(task));

        List<Task> tasks = taskService.getTasksByUserId("user1");

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        verify(taskRepository, times(1)).findTasksByAssigneeId("user1");
    }

    @Test
    void testGetTasksById() {
        when(taskRepository.findTaskByIdAndUserAccess(1L, "user1")).thenReturn(Optional.of(task));

        Optional<Task> foundTask = taskService.getTasksById(1L, "user1");

        assertTrue(foundTask.isPresent());
        assertEquals("Test Task", foundTask.get().getTitle());
        verify(taskRepository, times(1)).findTaskByIdAndUserAccess(1L, "user1");
    }

    @Test
    void testGetTasksByProjectId() {
        when(taskRepository.findAllTasksByProjectIdAndUserAccess(1L, "user1")).thenReturn(Arrays.asList(task));

        List<Task> tasks = taskService.getTasksByProjectId(1L, "user1");

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        verify(taskRepository, times(1)).findAllTasksByProjectIdAndUserAccess(1L, "user1");
    }
}