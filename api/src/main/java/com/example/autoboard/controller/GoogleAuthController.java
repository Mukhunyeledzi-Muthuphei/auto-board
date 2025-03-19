package com.example.autoboard.controller;

import org.springframework.web.bind.annotation.*;
import com.example.autoboard.service.GoogleAuthService;

@RestController
@RequestMapping("/auth")
public class GoogleAuthController {

    private final GoogleAuthService googleAuthService;

    public GoogleAuthController(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @GetMapping("/login-url")
    public String getLoginUrl() {
        return googleAuthService.getLoginUrl();
    }

    @GetMapping("/callback")
    public String handleCallback(@RequestParam("code") String code) {
        return googleAuthService.handleCallback(code);
    }
}
