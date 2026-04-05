package com.easymarkersapp.easymarkersapp.controller;

import com.easymarkersapp.easymarkersapp.dto.ProjectCreateRequest;
import com.easymarkersapp.easymarkersapp.model.Project;
import com.easymarkersapp.easymarkersapp.model.User;
import com.easymarkersapp.easymarkersapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin() //> Указать адрес и порт фронта
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<?> getProjects() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Project> projects = projectService.findByOwnerId(currentUser.getId());
        return ResponseEntity.ok(projects);
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody ProjectCreateRequest createRequest) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project project = new Project();
        project.setTitle(createRequest.getTitle());
        project.setDescription(createRequest.getDescription());
        project.setOwnerId(currentUser.getId());
        Project newProject = projectService.save(project);

        return ResponseEntity.ok(newProject);
    }
}
