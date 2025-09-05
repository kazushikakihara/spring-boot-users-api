package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

class User {
    private Long id;
    private String name;

    public User(Long id, String name) { this.id = id; this.name = name; }

    public Long getId() { return id; }
    public String getName() { return name; }
}

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final List<User> users = List.of(
            new User(1L, "Alice"),
            new User(2L, "Bob"),
            new User(3L, "Charlie")
    );

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
