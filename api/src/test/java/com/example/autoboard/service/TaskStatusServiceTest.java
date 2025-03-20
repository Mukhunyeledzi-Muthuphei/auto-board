package com.example.autoboard.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.autoboard.entity.TaskStatus;
import com.example.autoboard.repository.TaskStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

public class TaskStatusServiceTest {

    @Mock
    private TaskStatusRepository taskStatusRepository;

    @InjectMocks
    private TaskStatusService taskStatusService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTaskStatuses() {
        // Arrange
        TaskStatus status1 = new TaskStatus();
        status1.setName("To Do");
        TaskStatus status2 = new TaskStatus();
        status2.setName("Done");
        List<TaskStatus> statuses = Arrays.asList(status1, status2);

        when(taskStatusRepository.findAll()).thenReturn(statuses);

        // Act
        List<TaskStatus> result = taskStatusService.getAllTaskStatuses();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("To Do", result.get(0).getName());
        assertEquals("Done", result.get(1).getName());
    }

    @Test
    void testGetTaskStatusById() {
        // Arrange
        TaskStatus status = new TaskStatus();
        status.setName("In Progress");
        status.setId(1L);

        when(taskStatusRepository.findById(1L)).thenReturn(Optional.of(status));

        // Act
        Optional<TaskStatus> result = taskStatusService.getTaskStatusById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("In Progress", result.get().getName());
    }

    @Test
    void testGetTaskStatusByIdNotFound() {
        // Arrange
        when(taskStatusRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<TaskStatus> result = taskStatusService.getTaskStatusById(1L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testGetTaskStatusByName() {
        // Arrange
        TaskStatus status = new TaskStatus();
        status.setName("Completed");

        when(taskStatusRepository.findByName("Completed")).thenReturn(status);

        // Act
        TaskStatus result = taskStatusService.getTaskStatusByName("Completed");

        // Assert
        assertNotNull(result);
        assertEquals("Completed", result.getName());
    }

    @Test
    void testGetTaskStatusByNameNotFound() {
        // Arrange
        when(taskStatusRepository.findByName("Nonexistent")).thenReturn(null);

        // Act
        TaskStatus result = taskStatusService.getTaskStatusByName("Nonexistent");

        // Assert
        assertNull(result);
    }
}