package com.example.autoboard.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class GoogleAuthServiceTest {

    @InjectMocks
    private GoogleAuthService googleAuthService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(googleAuthService, "clientId", "test-client-id");
        ReflectionTestUtils.setField(googleAuthService, "clientSecret", "test-client-secret");
        ReflectionTestUtils.setField(googleAuthService, "redirectUri", "http://localhost/callback");
        ReflectionTestUtils.setField(googleAuthService, "tokenUrl", "http://example.com/token");
        ReflectionTestUtils.setField(googleAuthService, "authUrl", "http://example.com/auth");
    }

    @Test
    void testGetLoginUrl() {
        String loginUrl = googleAuthService.getLoginUrl();
        assertTrue(loginUrl.contains("client_id=test-client-id"));
        assertTrue(loginUrl.contains("redirect_uri=http%3A%2F%2Flocalhost%2Fcallback"));
        assertTrue(loginUrl.contains("response_type=code"));
    }

}