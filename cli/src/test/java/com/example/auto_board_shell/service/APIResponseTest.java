package com.example.auto_board_shell.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class APIResponseTest {

    @Test
    void testAPIResponseConstructorWithStatusCodeAndMessage() {
        // Arrange
        int expectedStatusCode = 200;
        String expectedMessage = "Success";

        // Act
        APIResponse<String> response = new APIResponse<>(expectedStatusCode, expectedMessage);

        // Assert
        assertEquals(expectedStatusCode, response.getStatusCode());
        assertEquals(expectedMessage, response.getMessage());
        assertNull(response.getData()); // Data should be null for this constructor
    }

    @Test
    void testAPIResponseConstructorWithAllFields() {
        // Arrange
        int expectedStatusCode = 200;
        String expectedMessage = "Success";
        String expectedData = "Some data";

        // Act
        APIResponse<String> response = new APIResponse<>(expectedStatusCode, expectedMessage, expectedData);

        // Assert
        assertEquals(expectedStatusCode, response.getStatusCode());
        assertEquals(expectedMessage, response.getMessage());
        assertEquals(expectedData, response.getData());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        APIResponse<String> response = new APIResponse<>();

        // Act
        response.setStatusCode(404);
        response.setMessage("Not Found");
        response.setData("No data available");

        // Assert
        assertEquals(404, response.getStatusCode());
        assertEquals("Not Found", response.getMessage());
        assertEquals("No data available", response.getData());
    }

    @Test
    void testToString() {
        // Arrange
        APIResponse<String> response = new APIResponse<>(200, "Success", "Some data");

        // Act
        String responseString = response.toString();

        // Assert
        assertTrue(responseString.contains("statusCode=200"));
        assertTrue(responseString.contains("message='Success'"));
        assertTrue(responseString.contains("data=Some data"));
    }
}
