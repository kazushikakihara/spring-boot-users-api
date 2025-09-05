package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

class User {
    private Long id;
    private String name;

    // Jackson 用にデフォルトコンストラクタを用意
    public User() {}

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }

    // Jackson が @RequestBody をバインドできるよう setter も用意
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
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

    // 一覧
    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }

    // 1件取得
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return users.stream()
            .filter(u -> u.getId().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 追加
    @PostMapping
    public User create(@RequestBody User u) {
        long id = seq.incrementAndGet();
        User created = new User(id, u.getName());
        users.add(created);
        return created;
    }

    // 更新
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User u) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                User updated = new User(id, u.getName());
                users.set(i, updated);
                return ResponseEntity.ok(updated);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // 削除
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        return removed ? ResponseEntity.noContent().build()
                       : ResponseEntity.notFound().build();
    }
}
