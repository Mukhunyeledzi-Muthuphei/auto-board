package com.example.helpers;

import com.google.auth.oauth2.TokenVerifier;
import com.google.auth.oauth2.TokenVerifier.VerificationException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Base64;
import java.util.Map;

public class TokenHelper {
    public static boolean isValidIdToken(String clientId, String idToken) {
        try {
            TokenVerifier tokenVerifier = TokenVerifier.newBuilder().setAudience(clientId).build();
            tokenVerifier.verify(idToken);
            return true;
        } catch (VerificationException e) {
            return false;
        }
    }

    public static Map<String, Object> parseIdToken(String idToken) {
        String[] parts = idToken.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
        JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();

        return Map.of(
                "sub", jsonObject.get("sub").getAsString(),
                "given_name", jsonObject.get("given_name").getAsString(),
                "family_name", jsonObject.get("family_name").getAsString());
    }

    public static String extractUserIdFromToken(String idToken) {
        Map<String, Object> tokenPayload = parseIdToken(idToken);
        return (String) tokenPayload.get("sub");
    }
}
