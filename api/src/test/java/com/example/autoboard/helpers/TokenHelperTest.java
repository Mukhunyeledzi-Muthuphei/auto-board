package com.example.autoboard.helpers;

import com.google.auth.oauth2.TokenVerifier;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenHelperTest {

    private static final String VALID_CLIENT_ID = "valid-client-id";
    private static final String VALID_ID_TOKEN = "valid.id.token";
    private static final String INVALID_ID_TOKEN = "invalid.id.token";

    private TokenVerifier.Builder tokenVerifierBuilder;
    private TokenVerifier tokenVerifier;

    @BeforeEach
    void setUp() {
        tokenVerifierBuilder = mock(TokenVerifier.Builder.class);
        tokenVerifier = mock(TokenVerifier.class);
    }

    @Test
    void testIsValidIdTokenValidToken() throws TokenVerifier.VerificationException {
        try (MockedStatic<TokenVerifier> mockedTokenVerifier = Mockito.mockStatic(TokenVerifier.class)) {
            // Mock the static method newBuilder() to return the mocked builder
            mockedTokenVerifier.when(TokenVerifier::newBuilder).thenReturn(tokenVerifierBuilder);

            // Mock the builder's setAudience method to return the builder itself
            when(tokenVerifierBuilder.setAudience(VALID_CLIENT_ID)).thenReturn(tokenVerifierBuilder);

            // Mock the builder's build method to return the mocked TokenVerifier
            when(tokenVerifierBuilder.build()).thenReturn(tokenVerifier);

            // Mock the verify method to simulate a successful verification
            when(tokenVerifier.verify(VALID_ID_TOKEN)).thenReturn(null);

            // Test the method
            assertTrue(TokenHelper.isValidIdToken(VALID_CLIENT_ID, VALID_ID_TOKEN));
        }
    }

    @Test
    void testIsValidIdTokenInvalidToken() throws TokenVerifier.VerificationException {
        try (MockedStatic<TokenVerifier> mockedTokenVerifier = Mockito.mockStatic(TokenVerifier.class)) {
            // Mock the static method newBuilder() to return the mocked builder
            mockedTokenVerifier.when(TokenVerifier::newBuilder).thenReturn(tokenVerifierBuilder);

            // Mock the builder's setAudience method to return the builder itself
            when(tokenVerifierBuilder.setAudience(VALID_CLIENT_ID)).thenReturn(tokenVerifierBuilder);

            // Mock the builder's build method to return the mocked TokenVerifier
            when(tokenVerifierBuilder.build()).thenReturn(tokenVerifier);

            // Mock the verify method to throw an exception
            when(tokenVerifier.verify(INVALID_ID_TOKEN))
                    .thenThrow(new TokenVerifier.VerificationException("Invalid token"));

            // Test the method
            assertFalse(TokenHelper.isValidIdToken(VALID_CLIENT_ID, INVALID_ID_TOKEN));
        }
    }

    @Test
    void testParseIdToken() {
        String payload = "{\"sub\":\"12345\",\"given_name\":\"John\",\"family_name\":\"Doe\"}";
        String encodedPayload = Base64.getUrlEncoder().encodeToString(payload.getBytes());
        String idToken = "header." + encodedPayload + ".signature";

        Map<String, Object> parsedToken = TokenHelper.parseIdToken(idToken);

        assertNotNull(parsedToken);
        assertEquals("12345", parsedToken.get("sub"));
        assertEquals("John", parsedToken.get("given_name"));
        assertEquals("Doe", parsedToken.get("family_name"));
    }

    @Test
    void testExtractUserIdFromToken() {
        String payload = "{\"sub\":\"12345\",\"given_name\":\"John\",\"family_name\":\"Doe\"}";
        String encodedPayload = Base64.getUrlEncoder().encodeToString(payload.getBytes());
        String idToken = "header." + encodedPayload + ".signature";

        String userId = TokenHelper.extractUserIdFromToken(idToken);

        assertNotNull(userId);
        assertEquals("12345", userId);
    }
}