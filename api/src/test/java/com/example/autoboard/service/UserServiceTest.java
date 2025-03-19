package com.example.autoboard.service;

import com.example.autoboard.entity.User;
import com.example.autoboard.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId("1");
        user.setFirstName("John");
        user.setLastName("Doe");
    }

    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user.getId(), users.get(0).getId());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserById() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());

        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    public void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);
        assertNotNull(createdUser);
        assertEquals(user.getId(), createdUser.getId());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUser() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User userDetails = new User();
        userDetails.setFirstName("Jane");
        userDetails.setLastName("Smith");

        User updatedUser = userService.updateUser(user.getId(), userDetails);
        assertNotNull(updatedUser);
        assertEquals(user.getId(), updatedUser.getId());
        assertEquals("Jane", updatedUser.getFirstName());
        assertEquals("Smith", updatedUser.getLastName());

        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUserNotFound() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        User userDetails = new User();
        userDetails.setFirstName("Jane");
        userDetails.setLastName("Smith");

        Exception exception = assertThrows(RuntimeException.class,
                () -> userService.updateUser(user.getId(), userDetails));

        String expectedMessage = "User not found with id " + user.getId();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(0)).save(any(User.class));
    }
}