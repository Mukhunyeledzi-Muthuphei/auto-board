package com.example.autoboard.helpers;

import com.google.auth.oauth2.TokenVerifier;
import com.google.auth.oauth2.TokenVerifier.VerificationException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenHelperTest {

    @Test
    void testIsValidIdToken_ValidToken() throws VerificationException {
        String clientId = "test-client-id";
        String idToken = "valid-token";

        try (MockedStatic<TokenVerifier> mockedTokenVerifier = mockStatic(TokenVerifier.class)) {
            TokenVerifier mockVerifier = mock(TokenVerifier.class);
            mockedTokenVerifier.when(() -> TokenVerifier.newBuilder().setAudience(clientId).build())
                    .thenReturn(mockVerifier);

            doNothing().when(mockVerifier).verify(idToken);

            boolean result = TokenHelper.isValidIdToken(clientId, idToken);
            assertTrue(result);
        }
    }

    @Test
    void testIsValidIdToken_InvalidToken() throws VerificationException {
        String clientId = "test-client-id";
        String idToken = "invalid-token";

        try (MockedStatic<TokenVerifier> mockedTokenVerifier = mockStatic(TokenVerifier.class)) {
            TokenVerifier mockVerifier = mock(TokenVerifier.class);
            mockedTokenVerifier.when(() -> TokenVerifier.newBuilder().setAudience(clientId).build())
                    .thenReturn(mockVerifier);

            doThrow(new VerificationException("Invalid token")).when(mockVerifier).verify(idToken);

            boolean result = TokenHelper.isValidIdToken(clientId, idToken);
            assertFalse(result);
        }
    }

    @Test
    void testParseIdToken() {
        String idToken = "header." +
                Base64.getUrlEncoder().encodeToString(
                        "{\"sub\":\"12345\",\"given_name\":\"John\",\"family_name\":\"Doe\"}".getBytes())
                +
                ".signature";

        Map<String, Object> result = TokenHelper.parseIdToken(idToken);

        assertEquals("12345", result.get("sub"));
        assertEquals("John", result.get("given_name"));
        assertEquals("Doe", result.get("family_name"));
    }

    @Test
    void testExtractUserIdFromToken() {
        String idToken = "header." +
                Base64.getUrlEncoder().encodeToString(
                        "{\"sub\":\"12345\",\"given_name\":\"John\",\"family_name\":\"Doe\"}".getBytes())
                +
                ".signature";

        String userId = TokenHelper.extractUserIdFromToken(idToken);

        assertEquals("12345", userId);
    }
}