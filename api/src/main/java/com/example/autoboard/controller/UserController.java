package com.example.autoboard.controller;

import com.example.autoboard.entity.User;
import com.example.autoboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import com.example.autoboard.helpers.TokenHelper;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Value("${google.client.id}")
    private String clientId;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content if no users are found
        }
        return ResponseEntity.ok(users); // Return 200 OK with the list of users
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
