package com.example.auto_board_shell.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.auto_board_shell.helpers.CurrentUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoogleAuthServiceTest {

    private static final String TEST_ID_TOKEN = "header.payload.signature";
    private static final String TEST_PAYLOAD = "{\"given_name\": \"John\", \"sub\": \"12345\"}";

    @BeforeEach
    void setup() {
        CurrentUser.setToken(null);
        CurrentUser.setUserName(null);
        CurrentUser.setId(null);
    }

    @Test
    void testDecodeAndStoreUserInfo() {
        try (MockedStatic<JWT> jwtMock = Mockito.mockStatic(JWT.class)) {
            // Mock JWT decoding
            DecodedJWT decodedJWT = mock(DecodedJWT.class);
            when(decodedJWT.getPayload()).thenReturn(java.util.Base64.getEncoder().encodeToString(TEST_PAYLOAD.getBytes()));

            jwtMock.when(() -> JWT.decode(TEST_ID_TOKEN)).thenReturn(decodedJWT);

            // Call the method
            GoogleAuthService.decodeAndStoreUserInfo(TEST_ID_TOKEN);

            // Verify CurrentUser is set correctly
            assertEquals(TEST_ID_TOKEN, CurrentUser.getToken());
            assertEquals("John", CurrentUser.getUserName());
            assertEquals("12345", CurrentUser.getId());

            // Verify the file was written
            File file = new File("user_info.txt");
            assertTrue(file.exists());

            String content = Files.readString(file.toPath());
            assertTrue(content.contains("ID Token: " + TEST_ID_TOKEN));
            assertTrue(content.contains("User Info: " + TEST_PAYLOAD));

            // Cleanup
            file.delete();
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test
    void testClearUserInfo() {
        // Set user info first
        CurrentUser.setToken("some-token");
        CurrentUser.setUserName("John");
        CurrentUser.setId("12345");

        // Create a dummy user info file
        try (FileWriter fileWriter = new FileWriter("user_info.txt")) {
            fileWriter.write("Test data");
        } catch (IOException e) {
            fail("Failed to create test file.");
        }

        // Call the method
        GoogleAuthService.clearUserInfo();

        // Verify user info is cleared
        assertNull(CurrentUser.getToken());
        assertNull(CurrentUser.getUserName());
        assertNull(CurrentUser.getId());

        // Verify file is cleared
        File file = new File("user_info.txt");
        assertTrue(file.exists());
        assertEquals(0, file.length());

        // Cleanup
        file.delete();
    }
}
