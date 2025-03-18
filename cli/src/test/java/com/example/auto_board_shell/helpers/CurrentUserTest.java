package com.example.auto_board_shell.helpers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrentUserTest {

    @BeforeEach
    void setUp() {
        // Clear any set values before each test
        CurrentUser.clear();
    }

    @Test
    void testSetAndGetToken() {
        // Arrange
        String expectedToken = "my-token";

        // Act
        CurrentUser.setToken(expectedToken);

        // Assert
        assertEquals(expectedToken, CurrentUser.getToken());
    }

    @Test
    void testSetAndGetUserName() {
        // Arrange
        String expectedUserName = "user123";

        // Act
        CurrentUser.setUserName(expectedUserName);

        // Assert
        assertEquals(expectedUserName, CurrentUser.getUserName());
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        String expectedId = "12345";

        // Act
        CurrentUser.setId(expectedId);

        // Assert
        assertEquals(expectedId, CurrentUser.getId());
    }

    @Test
    void testClear() {
        // Arrange
        String token = "my-token";
        String userName = "user123";
        String id = "12345";

        // Set values
        CurrentUser.setToken(token);
        CurrentUser.setUserName(userName);
        CurrentUser.setId(id);

        // Act
        CurrentUser.clear();

        // Assert
        assertNull(CurrentUser.getToken());
        assertNull(CurrentUser.getUserName());
        assertNull(CurrentUser.getId());
    }
}
