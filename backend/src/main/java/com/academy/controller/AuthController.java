package com.academy.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.entity.User;
import com.academy.service.AuthService;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        service.register(user);
        return "Registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        service.login(user.getEmail(), user.getPassword());
        return "Login Success";
    }
}
