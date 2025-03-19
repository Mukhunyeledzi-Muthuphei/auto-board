package com.example.autoboard.controller;

import com.example.autoboard.entity.User;
import com.example.autoboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(201).body(createdUser); // Return 201 Created with the created user
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser); // Return 200 OK with the updated user
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if the user does not exist
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id, @RequestHeader("Authorization") String token) {
        if (!TokenHelper.isValidIdToken(clientId, token)) {
            return ResponseEntity.status(403).build(); // Return 403 Forbidden if the token is invalid
        }
        if (!TokenHelper.parseIdToken(token).get("sub").equals(id)) {
            return ResponseEntity.status(403).build(); // Return 403 Forbidden if the token does not match the user ID
        }
        userService.deleteUser(id);
        return ResponseEntity.ok().build(); // Return 200 OK after successful deletion
    }
}
