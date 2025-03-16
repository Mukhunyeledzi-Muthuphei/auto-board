package com.example.autoboard.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectTest {

    private Project project;

    @BeforeEach
    public void setUp() {
        project = new Project();
        project.setId(1L);
        project.setName("Project 1");
        project.setDescription("Description 1");
        project.setStatusId(1L);
        project.setOwner(new User("1", "John", "Doe"));
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(1L, project.getId());
        assertEquals("Project 1", project.getName());
        assertEquals("Description 1", project.getDescription());
        assertEquals(1L, project.getStatusId());
        assertEquals(new User("1", "John", "Doe"), project.getOwner());

        project.setName("Project 2");
        project.setDescription("Description 2");
        project.setStatusId(2L);
        project.setOwner(new User("2", "Jane", "Smith"));
        project.setId(2L);

        assertEquals(2L, project.getId());
        assertEquals("Project 2", project.getName());
        assertEquals("Description 2", project.getDescription());
        assertEquals(2L, project.getStatusId());
        assertEquals(new User("2", "Jane", "Smith"), project.getOwner());
    }

    @Test
    public void testProjectEquality() {
        Project anotherProject = new Project();
        anotherProject.setId(1L);
        anotherProject.setName("Project 1");

        assertEquals(project, anotherProject);
    }

    @Test
    public void testProjectHashCode() {
        Project anotherProject = new Project();
        anotherProject.setId(1L);
        anotherProject.setName("Project 1");

        assertEquals(project.hashCode(), anotherProject.hashCode());
    }

}
