package com.example.autoboard.controller;

import com.example.autoboard.entity.Project;
import com.example.autoboard.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.autoboard.entity.User;

class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    private MockMvc mockMvc;

    private Project project;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
        project = new Project();
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatusId(1L);
        project.setOwner(new User("1", "John", "Doe"));
    }

    @Test
    void testGetAllProjects() throws Exception {
        List<Project> projects = Arrays.asList(project);
        when(projectService.getAllProjects()).thenReturn(projects);

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Project"));

        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    void testGetProjectById() throws Exception {
        when(projectService.getProjectById(1L)).thenReturn(project);

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Project"));

        verify(projectService, times(1)).getProjectById(1L);
    }

    @Test
    void testCreateProject() throws Exception {
        when(projectService.createProject(any(Project.class))).thenReturn(project);

        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Test Project\", \"description\": \"Test Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Project"));

        verify(projectService, times(1)).createProject(any(Project.class));
    }

    // @Test
    // void testUpdateProject() throws Exception {
    // when(projectService.updateProject(eq(1L),
    // any(Project.class))).thenReturn(project);

    // mockMvc.perform(put("/api/projects/1")
    // .contentType(MediaType.APPLICATION_JSON)
    // .content("{\"name\": \"Updated Project\", \"description\": \"Updated
    // Description\"}"))
    // .andExpect(status().isOk())
    // .andExpect(jsonPath("$.name").value("Test Project"));

    // verify(projectService, times(1)).updateProject(eq(1L), any(Project.class));
    // }

    // @Test
    // void testDeleteProject() throws Exception {
    // when(projectService.deleteProject(1L)).thenReturn(true);

    // mockMvc.perform(delete("/api/projects/1"))
    // .andExpect(status().isNoContent());

    // verify(projectService, times(1)).deleteProject(1L);
    // }
}