package com.example.autoboard;

import com.example.autoboard.controller.TaskController;
import com.example.autoboard.entity.*;
import com.example.autoboard.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

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
        assignee.setId("1");
        assignee.setFirstName("John");
        assignee.setLastName("Doe");

        task.setStatus(status);
        task.setProject(project);
        task.setAssignee(assignee);
    }

    @Test
    void testGetAllTasks() {
        // Arrange
        when(taskService.getAlltasks()).thenReturn(Arrays.asList(task));

        // Act
        List<Task> tasks = taskController.getAllTasks();

        // Assert
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
        assertEquals("In Progress", tasks.get(0).getStatus().getName());
        assertEquals("Project Alpha", tasks.get(0).getProject().getName());
        assertEquals("John", tasks.get(0).getAssignee().getFirstName());
        assertEquals("Doe", tasks.get(0).getAssignee().getLastName());
        verify(taskService, times(1)).getAlltasks();
    }

    @Test
    void testGetTaskById() {
        // Arrange
        when(taskService.getTaskById(1L)).thenReturn(task);

        // Act
        ResponseEntity<Task> response = taskController.getTaskById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Task", response.getBody().getTitle());
        assertEquals("In Progress", response.getBody().getStatus().getName());
        assertEquals("Project Alpha", response.getBody().getProject().getName());
        assertEquals("John", response.getBody().getAssignee().getFirstName());
        assertEquals("Doe", response.getBody().getAssignee().getLastName());
        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void testGetTasksByStatus() {
        // Arrange
        when(taskService.getTasksByStatus(1L)).thenReturn(Arrays.asList(task));

        // Act
        ResponseEntity<List<Task>> response = taskController.getTasksByStatus(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Task", response.getBody().get(0).getTitle());
        assertEquals("In Progress", response.getBody().get(0).getStatus().getName());
        assertEquals("Project Alpha", response.getBody().get(0).getProject().getName());
        assertEquals("John", response.getBody().get(0).getAssignee().getFirstName());
        assertEquals("Doe", response.getBody().get(0).getAssignee().getLastName());
        verify(taskService, times(1)).getTasksByStatus(1L);
    }

    @Test
    void testCreateTask() {
        // Arrange
        when(taskService.createTask(task)).thenReturn(task);

        // Act
        ResponseEntity<Task> response = taskController.createTask(task);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Task", response.getBody().getTitle());
        assertEquals("In Progress", response.getBody().getStatus().getName());
        assertEquals("Project Alpha", response.getBody().getProject().getName());
        assertEquals("John", response.getBody().getAssignee().getFirstName());
        assertEquals("Doe", response.getBody().getAssignee().getLastName());
        verify(taskService, times(1)).createTask(task);
    }

    @Test
    void testUpdateTask() {
        // Arrange
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("This is an updated task");
        updatedTask.setStatus(status);
        updatedTask.setProject(project);
        updatedTask.setAssignee(assignee);

        when(taskService.updateTask(1L, updatedTask)).thenReturn(updatedTask);

        // Act
        ResponseEntity<Task> response = taskController.updateTask(1L, updatedTask);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Task", response.getBody().getTitle());
        assertEquals("This is an updated task", response.getBody().getDescription());
        assertEquals("In Progress", response.getBody().getStatus().getName());
        assertEquals("Project Alpha", response.getBody().getProject().getName());
        assertEquals("John", response.getBody().getAssignee().getFirstName());
        assertEquals("Doe", response.getBody().getAssignee().getLastName());
        verify(taskService, times(1)).updateTask(1L, updatedTask);
    }

    @Test
    void testDeleteTask() {
        // Arrange
        doNothing().when(taskService).deleteTask(1L);

        // Act
        ResponseEntity<Void> response = taskController.deleteTask(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskService, times(1)).deleteTask(1L);
    }
}