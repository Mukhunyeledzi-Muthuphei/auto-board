package com.example.autoboard.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.example.helpers.TokenHelper;
import com.example.autoboard.entity.User;
import com.example.autoboard.repository.UserRepository;
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
