package com.example.autoboard.controller;

import org.springframework.web.bind.annotation.*;
import com.example.autoboard.service.GoogleAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/auth")
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
