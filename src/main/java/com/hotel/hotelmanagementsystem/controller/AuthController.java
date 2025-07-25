package com.hotel.hotelmanagementsystem.controller;

import com.hotel.hotelmanagementsystem.model.User;
import com.hotel.hotelmanagementsystem.service.AuthService;
import com.hotel.hotelmanagementsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Base path for authentication endpoints
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:8080"}) // Allow frontend origins
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    // Add a constructor to log when the controller is initialized
    public AuthController() {
        System.out.println("AuthController: Initializing AuthController bean.");
    }

    // Endpoint for user registration
    // POST http://localhost:8080/api/auth/register
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody User user) {
        System.out.println("AuthController: Received registration request for email: " + user.getEmail());
        User registeredUser = userService.registerUser(user);
        String token = authService.generateToken(registeredUser);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully!");
        response.put("user", registeredUser);
        response.put("token", token);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Endpoint for user login
    // POST http://localhost:8080/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User loginRequest) {
        System.out.println("AuthController: Received login request for email: " + loginRequest.getEmail());
        User user = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        
        String token = authService.generateToken(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful!");
        response.put("user", user);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
