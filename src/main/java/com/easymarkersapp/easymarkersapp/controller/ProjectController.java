package com.easymarkersapp.easymarkersapp.controller;

import com.easymarkersapp.easymarkersapp.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin() //> Указать адрес и порт фронта
public class ProjectController {

    @GetMapping
    public ResponseEntity<?> getProjects() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok("Projects for user: " + currentUser.getUsername());
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody String projectName) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok("Project '" + projectName + "' created for " + currentUser.getUsername());
    }
}
