package com.example.autoboard.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.example.helpers.TokenValidator;

@RestController
@RequestMapping("/auth")
public class GoogleAuthController {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Value("${google.auth.url}")
    private String authUrl;

    @Value("${google.token.url}")
    private String tokenUrl;

    @Value("${google.user.info.url}")
    private String userInfoUrl;

    @GetMapping("/login-url")
    public String getLoginUrl() {
        return authUrl + "?client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&response_type=code" +
                "&scope=" + URLEncoder.encode("email profile", StandardCharsets.UTF_8) +
                "&access_type=offline";
    }

    @GetMapping("/callback")
    public String handleCallback(@RequestParam("code") String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> requestBody = Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "redirect_uri", redirectUri,
                "code", code,
                "grant_type", "authorization_code");

        Map<String, Object> response = restTemplate.postForObject(tokenUrl, requestBody, Map.class);
        String idToken = (String) response.get("id_token");
        if (TokenValidator.isValidIdToken(clientId, idToken)) {
            return idToken;
        } else {
            return "Invalid ID token";
        }
    }
}
