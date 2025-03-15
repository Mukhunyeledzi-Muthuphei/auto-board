package com.example.autoboard.repository;

import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.transaction.annotation.Transactional;
import com.example.autoboard.repository.UserRepository;

@SpringBootTest
@Transactional
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    private Project project;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("1", "John", "Doe");
        userRepository.save(user);
        project = new Project();
        project.setName("Test Project");
        project.setStatusId(1L);
        project.setOwner(user);
        project.setDescription("Test Description");

        projectRepository.save(project);
    }

    @Test
    public void testFindByName() {
        List<Project> projects = projectRepository.findByName("Test Project");
        assertFalse(projects.isEmpty());
        assertEquals("Test Project", projects.get(0).getName());
    }

    @Test
    public void testFindByStatusId() {
        List<Project> projects = projectRepository.findByStatusId(1L);
        assertFalse(projects.isEmpty());
        assertEquals(1L, projects.get(0).getStatusId());
    }

    @Test
    public void testFindByOwner() {
        List<Project> projects = projectRepository.findByOwner(user);
        assertFalse(projects.isEmpty());
        assertEquals(user, projects.get(0).getOwner());
    }

    @Test
    public void testFindByNameContaining() {
        List<Project> projects = projectRepository.findByNameContaining("Test");
        assertFalse(projects.isEmpty());
        assertTrue(projects.get(0).getName().contains("Test"));
    }

    @Test
    public void testFindByDescriptionContaining() {
        List<Project> projects = projectRepository.findByDescriptionContaining("Description");
        assertFalse(projects.isEmpty());
        assertTrue(projects.get(0).getDescription().contains("Description"));
    }
}