package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

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

    private final List<User> users = new ArrayList<>(List.of(
        new User(1L, "Alice"),
        new User(2L, "Bob"),
        new User(3L, "Charlie")
    ));
    private final AtomicLong seq = new AtomicLong(3);

    @GetMapping
    public List<User> getAllUsers() { return users; }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return users.stream().filter(u -> u.getId().equals(id))
            .findFirst().map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public User create(@RequestBody User u) {
        long id = seq.incrementAndGet();
        users.add(new User(id, u.getName()));
        return users.get(users.size() - 1);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        return removed ? ResponseEntity.noContent().build()
                       : ResponseEntity.notFound().build();
    }
}
