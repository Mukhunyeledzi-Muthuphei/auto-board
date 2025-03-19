package com.example.autoboard.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.autoboard.entity.ProjectStatus;
import com.example.autoboard.repository.ProjectStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

public class ProjectStatusServiceTest {

    @Mock
    private ProjectStatusRepository projectStatusRepository;

    @InjectMocks
    private ProjectStatusService projectStatusService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProjectStatuses() {
        // Arrange: Mock the repository method to return a list of project statuses
        ProjectStatus status1 = new ProjectStatus();
        status1.setStatus("In Progress");
        ProjectStatus status2 = new ProjectStatus();
        status2.setStatus("Completed");
        List<ProjectStatus> statuses = Arrays.asList(status1, status2);

        when(projectStatusRepository.findAll()).thenReturn(statuses);

        // Act: Call the service method
        List<ProjectStatus> result = projectStatusService.getAllProjectStatuses();

        // Assert: Verify the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("In Progress", result.get(0).getName());
        assertEquals("Completed", result.get(1).getName());
    }

    @Test
    void testGetProjectStatusById() {
        // Arrange: Mock the repository method to return a specific project status
        ProjectStatus status = new ProjectStatus();
        status.setStatus("In Progress");
        status.setId(1L);

        when(projectStatusRepository.findById(1L)).thenReturn(Optional.of(status));

        // Act: Call the service method
        Optional<ProjectStatus> result = projectStatusService.getProjectStatusById(1L);

        // Assert: Verify the result
        assertTrue(result.isPresent());
        assertEquals("In Progress", result.get().getName());
    }

    @Test
    void testGetProjectStatusById_notFound() {
        // Arrange: Mock the repository method to return an empty Optional
        when(projectStatusRepository.findById(1L)).thenReturn(Optional.empty());

        // Act: Call the service method
        Optional<ProjectStatus> result = projectStatusService.getProjectStatusById(1L);

        // Assert: Verify the result
        assertFalse(result.isPresent());
    }

    @Test
    void testGetProjectStatusByName() {
        // Arrange: Mock the repository method to return a specific project status
        ProjectStatus status = new ProjectStatus();
        status.setStatus("In Progress");

        when(projectStatusRepository.findByName("In Progress")).thenReturn(status);

        // Act: Call the service method
        ProjectStatus result = projectStatusService.getProjectStatusByName("In Progress");

        // Assert: Verify the result
        assertNotNull(result);
        assertEquals("In Progress", result.getName());
    }

    @Test
    void testGetProjectStatusByName_notFound() {
        // Arrange: Mock the repository method to return null
        when(projectStatusRepository.findByName("Nonexistent")).thenReturn(null);

        // Act: Call the service method
        ProjectStatus result = projectStatusService.getProjectStatusByName("Nonexistent");

        // Assert: Verify the result
        assertNull(result);
    }
}