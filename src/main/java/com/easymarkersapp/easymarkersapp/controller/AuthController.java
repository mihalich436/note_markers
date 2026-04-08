package com.easymarkersapp.easymarkersapp.controller;

import com.easymarkersapp.easymarkersapp.dto.AuthResponse;
import com.easymarkersapp.easymarkersapp.dto.LoginRequest;
import com.easymarkersapp.easymarkersapp.dto.RegisterRequest;
import com.easymarkersapp.easymarkersapp.model.User;
import com.easymarkersapp.easymarkersapp.service.JwtService;
import com.easymarkersapp.easymarkersapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin() //> Указать адрес и порт фронта
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            if (!checkNewPassword(request.getPassword())) {
                return ResponseEntity.badRequest().body(new AuthResponse(null, null, "Weak password"));
            }
            User user = userService.register(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword()
            );
            String token = jwtService.generateToken(user.getEmail());
            return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), "Registration successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, null, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userService.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (userService.checkPassword(request.getPassword(), user.getPasswordHash())) {
                String token = jwtService.generateToken(user.getEmail());
                return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), "Login successful"));
            }
        }

        return ResponseEntity.status(401).body(new AuthResponse(null, null, "Invalid credentials"));
    }

    private boolean checkNewPassword(String rawPassword) {
        return rawPassword.length() > 0; //> temp
    }
}
