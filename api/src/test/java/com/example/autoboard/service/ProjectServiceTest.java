package com.example.autoboard.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.User;
import com.example.autoboard.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId("user123");

        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setOwner(user);
    }

    @Test
    void testGetAllProjects() {
        when(projectRepository.findAll()).thenReturn(Collections.singletonList(project));
        List<Project> projects = projectService.getAllProjects();
        assertEquals(1, projects.size());
        verify(projectRepository).findAll();
    }

    @Test
    void testGetAllProjectsByUserId() {
        when(projectRepository.findAllProjectsByUser("user123")).thenReturn(Collections.singletonList(project));
        List<Project> projects = projectService.getAllProjectsByUserId("user123");
        assertEquals(1, projects.size());
        verify(projectRepository).findAllProjectsByUser("user123");
    }

    @Test
    void testGetProjectByIdForUser() {
        when(projectRepository.findProjectByIdAndUser(1L, "user123")).thenReturn(Optional.of(project));
        Optional<Project> foundProject = projectService.getProjectByIdForUser(1L, "user123");
        assertTrue(foundProject.isPresent());
        assertEquals("Test Project", foundProject.get().getName());
    }

    @Test
    void testGetProjectById() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        Project foundProject = projectService.getProjectById(1L);
        assertNotNull(foundProject);
        assertEquals("Test Project", foundProject.getName());
    }

    @Test
    void testCreateProject() {
        when(projectRepository.save(project)).thenReturn(project);
        Project createdProject = projectService.createProject(project);
        assertNotNull(createdProject);
        assertEquals("Test Project", createdProject.getName());
    }

    @Test
    void testUpdateProjectSuccess() {
        Project updatedDetails = new Project();
        updatedDetails.setName("Updated Name");
        updatedDetails.setDescription("Updated Description");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project updatedProject = projectService.updateProject(1L, updatedDetails, "user123");
        assertNotNull(updatedProject);
        assertEquals("Updated Name", updatedProject.getName());
    }

    @Test
    void testUpdateProjectUnauthorized() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        Project updatedProject = projectService.updateProject(1L, new Project(), "wrongUser");
        assertNull(updatedProject);
    }

    @Test
    void testDeleteProjectSuccess() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        doNothing().when(projectRepository).deleteById(1L);

        boolean result = projectService.deleteProject(1L, "user123");
        assertTrue(result);
        verify(projectRepository).deleteById(1L);
    }

    @Test
    void testDeleteProjectUnauthorized() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        boolean result = projectService.deleteProject(1L, "wrongUser");
        assertFalse(result);
    }
}
