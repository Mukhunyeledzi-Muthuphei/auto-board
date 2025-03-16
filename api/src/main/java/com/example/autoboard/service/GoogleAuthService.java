package com.example.autoboard.service;

import com.example.autoboard.entity.User;
import com.example.autoboard.repository.UserRepository;
import com.example.helpers.TokenHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.example.autoboard.entity.User;

import java.util.Map;

@Service
public class GoogleAuthService {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Value("${google.token.url}")
    private String tokenUrl;

    @Value("${google.auth.url}")
    private String authUrl;

    private final UserRepository userRepository;

    public GoogleAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getLoginUrl() {
        return authUrl + "?client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "^&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "^&response_type=code" +
                "^&scope=" + URLEncoder.encode("email profile", StandardCharsets.UTF_8) +
                "^&access_type=offline";
    }

    public String handleCallback(String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> requestBody = Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "redirect_uri", redirectUri,
                "code", code,
                "grant_type", "authorization_code");

        Map<String, Object> response = restTemplate.postForObject(tokenUrl, requestBody, Map.class);
        String idToken = (String) response.get("id_token");
        if (TokenHelper.isValidIdToken(clientId, idToken)) {
            Map<String, Object> userInfo = TokenHelper.parseIdToken(idToken);
            String id = (String) userInfo.get("sub");
            String firstName = (String) userInfo.get("given_name");
            String lastName = (String) userInfo.get("family_name");

            User user = new User(id, firstName, lastName);
            userRepository.save(user);

            return idToken;
        } else {
            return "Invalid ID token";
        }
    }
}