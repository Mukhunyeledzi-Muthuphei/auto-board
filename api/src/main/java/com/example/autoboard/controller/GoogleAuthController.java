package com.example.autoboard.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.example.autoboard.helpers.TokenHelper;
import com.example.autoboard.entity.User;
import com.example.autoboard.repository.UserRepository;
import com.example.autoboard.service.GoogleAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/auth")
public class GoogleAuthController {

    private final GoogleAuthService googleAuthService;

    public GoogleAuthController(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @GetMapping("/login-url")
    public ResponseEntity<String> getLoginUrl() {
        String loginUrl = googleAuthService.getLoginUrl();
        return ResponseEntity.ok(loginUrl); // Return 200 OK with the login URL
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam("code") String code) {
        try {
            String response = googleAuthService.handleCallback(code);
            return ResponseEntity.ok(response); // Return 200 OK with the response
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error handling callback: " + e.getMessage()); // Return 500 Internal Server Error
        }
    }
}
