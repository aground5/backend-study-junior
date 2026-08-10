package com.gdgku.study.backend.controller;

import com.gdgku.study.backend.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final List<User> userList = new ArrayList<>();
    private long nextId = 1L;

    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setId(nextId++);
        userList.add(user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userList;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        for (User u : userList) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    @GetMapping("/param")
    public String getUserByQueryParam(@RequestParam String name) {
        return "Query Param name: " + name;
    }
}
