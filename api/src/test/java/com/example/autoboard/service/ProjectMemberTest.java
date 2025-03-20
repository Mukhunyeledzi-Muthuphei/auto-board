package com.example.autoboard.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.autoboard.entity.Project;
import com.example.autoboard.entity.ProjectMember;
import com.example.autoboard.entity.User;
import com.example.autoboard.repository.ProjectMemberRepository;
import com.example.autoboard.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProjectMemberServiceTest {

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectMemberService projectMemberService;

    private Project project;
    private User member;
    private ProjectMember projectMember;

    @BeforeEach
    void setUp() {
        User owner = new User("1", "Owner", "User");
        member = new User("2", "Member", "User");
        project = new Project("Test Project", "Description", null, owner);
        project.setId(100L);
        projectMember = new ProjectMember(project, member);
    }

    @Test
    void testGetProjectMemberByIdSuccess() {
        when(projectMemberRepository.findById(1L)).thenReturn(Optional.of(projectMember));
        when(projectRepository.findById(100L)).thenReturn(Optional.of(project));

        Optional<ProjectMember> result = projectMemberService.getProjectMemberById(1L, "1");
        assertTrue(result.isPresent());
    }

    @Test
    void testGetProjectMemberByIdNotOwner() {
        when(projectMemberRepository.findById(1L)).thenReturn(Optional.of(projectMember));
        when(projectRepository.findById(100L)).thenReturn(Optional.of(project));

        Optional<ProjectMember> result = projectMemberService.getProjectMemberById(1L, "3");
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetProjectMembersByProjectSuccess() {
        when(projectRepository.findById(100L)).thenReturn(Optional.of(project));
        when(projectMemberRepository.findByProject(project)).thenReturn(List.of(projectMember));

        List<ProjectMember> result = projectMemberService.getProjectMembersByProject(project, "1");
        assertFalse(result.isEmpty());
    }

    @Test
    void testCreateProjectMember() {
        when(projectMemberRepository.save(any(ProjectMember.class))).thenReturn(projectMember);
        ProjectMember result = projectMemberService.createProjectMember(project, member);
        assertNotNull(result);
    }

    @Test
    void testSaveProjectMemberSuccess() {
        when(projectRepository.findById(100L)).thenReturn(Optional.of(project));
        when(projectMemberRepository.save(any(ProjectMember.class))).thenReturn(projectMember);

        ProjectMember result = projectMemberService.saveProjectMember(projectMember, "1");
        assertNotNull(result);
    }

    @Test
    void testSaveProjectMemberNotOwner() {
        when(projectRepository.findById(100L)).thenReturn(Optional.of(project));
        ProjectMember result = projectMemberService.saveProjectMember(projectMember, "3");
        assertNull(result);
    }

    @Test
    void testDeleteProjectMemberSuccess() {
        when(projectRepository.findById(100L)).thenReturn(Optional.of(project));
        when(projectMemberRepository.findByProjectAndUser(project, member)).thenReturn(Optional.of(projectMember));

        projectMemberService.deleteProjectMember(projectMember, "1");
        verify(projectMemberRepository, times(1)).delete(projectMember);
    }

    @Test
    void testDeleteProjectMemberNotOwner() {
        when(projectRepository.findById(100L)).thenReturn(Optional.of(project));

        projectMemberService.deleteProjectMember(projectMember, "3");
        verify(projectMemberRepository, never()).delete(any(ProjectMember.class));
    }
}
