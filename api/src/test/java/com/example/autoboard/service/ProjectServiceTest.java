package com.example.autoboard.service;

import com.example.autoboard.entity.Project;
import com.example.autoboard.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.example.autoboard.entity.User;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatusId(1L);
        project.setOwner(new User("1", "John", "Doe"));
    }

    @Test
    void testGetAllProjects() {
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project));
        List<Project> projects = projectService.getAllProjects();
        assertNotNull(projects);
        assertEquals(1, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void testGetProjectById() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        Project foundProject = projectService.getProjectById(1L);
        assertNotNull(foundProject);
        assertEquals("Test Project", foundProject.getName());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateProject() {
        when(projectRepository.save(project)).thenReturn(project);
        Project createdProject = projectService.createProject(project);
        assertNotNull(createdProject);
        assertEquals("Test Project", createdProject.getName());
        verify(projectRepository, times(1)).save(project);
    }

    // @Test
    // void testUpdateProject() {
    // Project updatedDetails = new Project();
    // updatedDetails.setName("Updated Project");
    // updatedDetails.setDescription("Updated Description");

    // when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
    // when(projectRepository.save(project)).thenReturn(project);

    // Project updatedProject = projectService.updateProject(1L, updatedDetails);
    // assertNotNull(updatedProject);
    // assertEquals("Updated Project", updatedProject.getName());
    // assertEquals("Updated Description", updatedProject.getDescription());
    // verify(projectRepository, times(1)).findById(1L);
    // verify(projectRepository, times(1)).save(project);
    // }

    // @Test
    // void testDeleteProject() {
    // when(projectRepository.existsById(1L)).thenReturn(true);
    // doNothing().when(projectRepository).deleteById(1L);

    // boolean isDeleted = projectService.deleteProject(1L);
    // assertTrue(isDeleted);
    // verify(projectRepository, times(1)).existsById(1L);
    // verify(projectRepository, times(1)).deleteById(1L);
    // }
}