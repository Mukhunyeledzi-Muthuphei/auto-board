package com.example.helpers;

import com.google.auth.oauth2.TokenVerifier;
import com.google.auth.oauth2.TokenVerifier.VerificationException;

public class TokenValidator {
    public static boolean isValidIdToken(String clientId, String idToken) {
        try {
            TokenVerifier tokenVerifier = TokenVerifier.newBuilder().setAudience(clientId).build();
            tokenVerifier.verify(idToken);
            return true;
        } catch (VerificationException e) {
            return false;
        }
    }
}
