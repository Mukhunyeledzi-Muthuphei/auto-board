package com.example.autoboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/misc")
public class MiscellaneousController {

    @GetMapping("/health")
    public String healthCheck() {
        return "API is up and running!";
    }
}