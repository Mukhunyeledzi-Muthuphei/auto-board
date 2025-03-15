package com.example.autoboard;

import com.example.autoboard.controller.TaskController;
import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.Task;
import com.example.autoboard.entity.TaskStatus;
import com.example.autoboard.entity.User;
import com.example.autoboard.service.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TaskControllerTests {
      @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    private Task newTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();


        newTask = new Task(); 
        newTask.setId(1L);
        newTask.setTitle("Test Task");
        newTask.setDescription("Unit Tests for Tasks");

        TaskStatus status = new TaskStatus();
        status.setId(1L);
        status.setName("To Do");
        newTask.setStatus(status);

        Project project = new Project();
        project.setId(1L);
        project.setName("Project 1");
        newTask.setProject(project);

        User assignee = new User();
        assignee.setId(1L);
        assignee.setFirstName("John");
        assignee.setLastName("Doe");
        newTask.setAssignee(assignee);
    }

    @Test
    void getAllTasks() throws Exception {

        when(taskService.getAlltasks()).thenReturn(Arrays.asList(newTask));

        mockMvc.perform(get("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(newTask.getId()))
                .andExpect(jsonPath("$[0].title").value(newTask.getTitle()))
                .andExpect(jsonPath("$[0].description").value(newTask.getDescription()))
                .andExpect(jsonPath("$[0].status.id").value(newTask.getStatus().getId()))
                .andExpect(jsonPath("$[0].status.name").value(newTask.getStatus().getName()))
                .andExpect(jsonPath("$[0].project.id").value(newTask.getProject().getId()))
                .andExpect(jsonPath("$[0].project.name").value(newTask.getProject().getName()))
                .andExpect(jsonPath("$[0].assignee.id").value(newTask.getAssignee().getId()))
                .andExpect(jsonPath("$[0].assignee.firstName").value(newTask.getAssignee().getFirstName()))
                .andExpect(jsonPath("$[0].assignee.lastName").value(newTask.getAssignee().getLastName()));

        verify(taskService, times(1)).getAlltasks();
    }

    @Test
    void getTaskById() throws Exception {

        when(taskService.getTaskById(newTask.getId())).thenReturn(newTask);

        mockMvc.perform(get("/api/tasks/{id}", newTask.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newTask.getId()))
                .andExpect(jsonPath("$.title").value(newTask.getTitle()))
                .andExpect(jsonPath("$.description").value(newTask.getDescription()))
                .andExpect(jsonPath("$.status.id").value(newTask.getStatus().getId()))
                .andExpect(jsonPath("$.status.name").value(newTask.getStatus().getName()))
                .andExpect(jsonPath("$.project.id").value(newTask.getProject().getId()))
                .andExpect(jsonPath("$.project.name").value(newTask.getProject().getName()))
                .andExpect(jsonPath("$.assignee.id").value(newTask.getAssignee().getId()))
                .andExpect(jsonPath("$.assignee.firstName").value(newTask.getAssignee().getFirstName()))
                .andExpect(jsonPath("$.assignee.lastName").value(newTask.getAssignee().getLastName()));

        verify(taskService, times(1)).getTaskById(newTask.getId());
    }

    @Test
    void getTasksByStatus() throws Exception {

        when(taskService.getTasksByStatus(newTask.getId())).thenReturn(Arrays.asList(newTask));

        mockMvc.perform(get("/api/tasks/status/{statusId}", newTask.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(newTask.getId()))
            .andExpect(jsonPath("$[0].title").value(newTask.getTitle()))
            .andExpect(jsonPath("$[0].description").value(newTask.getDescription()))
            .andExpect(jsonPath("$[0].status.id").value(newTask.getStatus().getId()))
            .andExpect(jsonPath("$[0].status.name").value(newTask.getStatus().getName()))
            .andExpect(jsonPath("$[0].project.id").value(newTask.getProject().getId()))
            .andExpect(jsonPath("$[0].project.name").value(newTask.getProject().getName()))
            .andExpect(jsonPath("$[0].assignee.id").value(newTask.getAssignee().getId()))
            .andExpect(jsonPath("$[0].assignee.firstName").value(newTask.getAssignee().getFirstName()));

            verify(taskService, times(1)).getTasksByStatus(newTask.getId());
    }

    @Test
    void createTask() throws Exception {

        when(taskService.createTask(any(Task.class))).thenReturn(newTask);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"New Task\", \"description\": \"New Description\", \"status\": {\"id\": 1, \"name\": \"To Do\"}, \"project\": {\"id\": 1, \"name\": \"Project 1\"}, \"assignee\": {\"id\": 1, \"firstName\": \"John\", \"lastName\": \"Doe\"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newTask.getId()))
                .andExpect(jsonPath("$.title").value(newTask.getTitle()))
                .andExpect(jsonPath("$.description").value(newTask.getDescription()))
                .andExpect(jsonPath("$.status.id").value(newTask.getStatus().getId()))
                .andExpect(jsonPath("$.status.name").value(newTask.getStatus().getName()))
                .andExpect(jsonPath("$.project.id").value(newTask.getProject().getId()))
                .andExpect(jsonPath("$.project.name").value(newTask.getProject().getName()))
                .andExpect(jsonPath("$.assignee.id").value(newTask.getAssignee().getId()))
                .andExpect(jsonPath("$.assignee.firstName").value(newTask.getAssignee().getFirstName()))
                .andExpect(jsonPath("$.assignee.lastName").value(newTask.getAssignee().getLastName()));

        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    void updateTask() throws Exception {

        when(taskService.updateTask(anyLong(), any(Task.class))).thenReturn(newTask);

        mockMvc.perform(put("/api/tasks/{id}", newTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Updated Task\", \"description\": \"Updated Description\", \"status\": {\"id\": 1, \"name\": \"In Progress\"}, \"project\": {\"id\": 1, \"name\": \"Project 1\"}, \"assignee\": {\"id\": 1, \"firstName\": \"John\", \"lastName\": \"Doe\"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newTask.getId()))
                .andExpect(jsonPath("$.title").value(newTask.getTitle()))
                .andExpect(jsonPath("$.description").value(newTask.getDescription()))
                .andExpect(jsonPath("$.status.id").value(newTask.getStatus().getId()))
                .andExpect(jsonPath("$.status.name").value(newTask.getStatus().getName()))
                .andExpect(jsonPath("$.project.id").value(newTask.getProject().getId()))
                .andExpect(jsonPath("$.project.name").value(newTask.getProject().getName()))
                .andExpect(jsonPath("$.assignee.id").value(newTask.getAssignee().getId()))
                .andExpect(jsonPath("$.assignee.firstName").value(newTask.getAssignee().getFirstName()))
                .andExpect(jsonPath("$.assignee.lastName").value(newTask.getAssignee().getLastName()));

        verify(taskService, times(1)).updateTask(anyLong(), any(Task.class));
    }

    @Test
    void deleteTask() throws Exception {

        doNothing().when(taskService).deleteTask(newTask.getId());

        mockMvc.perform(delete("/api/tasks/{id}", newTask.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(newTask.getId());
    }
}