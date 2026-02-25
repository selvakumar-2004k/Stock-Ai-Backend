package com.example.demo.controller;// ... other imports
import com.example.demo.dto.LoginRequest;
import com.example.demo.entity.User;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user) {
        String message = service.register(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response; // Returns {"message": "User registered successfully"}
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        String token = service.login(request.getEmail(), request.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response; // Returns {"token": "eyJhbG..."}
    }
}