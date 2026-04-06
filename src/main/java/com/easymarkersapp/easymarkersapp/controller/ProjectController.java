package com.easymarkersapp.easymarkersapp.controller;

import com.easymarkersapp.easymarkersapp.dto.AuthResponse;
import com.easymarkersapp.easymarkersapp.dto.MapCreateRequest;
import com.easymarkersapp.easymarkersapp.dto.ProjectCreateRequest;
import com.easymarkersapp.easymarkersapp.model.Map;
import com.easymarkersapp.easymarkersapp.model.Project;
import com.easymarkersapp.easymarkersapp.model.User;
import com.easymarkersapp.easymarkersapp.service.MapService;
import com.easymarkersapp.easymarkersapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin() //> Указать адрес и порт фронта
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private MapService mapService;

    @GetMapping
    public ResponseEntity<?> getProjects() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Project> projects = projectService.findByOwnerId(currentUser.getId());
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProject(@PathVariable Long id) {
        Optional<Project> projectOptional = projectService.findById(id);

        if (projectOptional.isPresent()) {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Project project = projectOptional.get();
            System.out.println(project);
            if (currentUser.projectBelongUser(project)){
                return ResponseEntity.ok(project);
            }
            return ResponseEntity.status(403).body(new AuthResponse(null, null, "Invalid credentials"));
        }
        return ResponseEntity.status(404).body(new AuthResponse(null, null, "Invalid project id"));
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

    @GetMapping("/{projectId}/maps")
    public ResponseEntity<?> getMaps(@PathVariable Long projectId) {
        Optional<Project> projectOptional = projectService.findById(projectId);
        if (projectOptional.isPresent()) {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (currentUser.projectBelongUser(projectOptional.get())){
                return ResponseEntity.ok(projectOptional.get().getMaps());
            }
            return ResponseEntity.status(403).body(new AuthResponse(null, null, "Invalid credentials"));
        }
        return ResponseEntity.status(404).body(new AuthResponse(null, null, "Invalid project id"));
    }

    @PostMapping("/{projectId}/maps")
    public ResponseEntity<?> createMap(@RequestBody MapCreateRequest createRequest, @PathVariable Long projectId) {
        Optional<Project> projectOptional = projectService.findById(projectId);
        if (projectOptional.isPresent()) {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (currentUser.projectBelongUser(projectOptional.get())){
                Map map = new Map();
                map.setTitle(createRequest.getTitle());
                map.setDescription(createRequest.getDescription());
                map.setImageUrl(createRequest.getImageUrl());
                map.setProjectId(projectId);
//                Project project = new Project();
//                project.setId(projectId);
//                map.setProject(project);
                Map newMap = mapService.save(map);
                return ResponseEntity.ok(newMap);
            }
            return ResponseEntity.status(403).body(new AuthResponse(null, null, "Invalid credentials"));
        }
        return ResponseEntity.status(404).body(new AuthResponse(null, null, "Invalid project id"));
    }

    @PutMapping("/{projectId}/maps/{mapId}")
    public ResponseEntity<?> editMap(
            @RequestBody MapCreateRequest editRequest,
            @PathVariable Long projectId,
            @PathVariable Long mapId
    ) {
        System.out.println("editMap");
        Optional<Map> mapOptional = mapService.findByIdAndProjectId(mapId, projectId);
        if (mapOptional.isPresent()) {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Map map = mapOptional.get();
            System.out.println(map);
            if (currentUser.projectBelongUser(map.getProject())){
            map.setTitle(editRequest.getTitle());
            map.setDescription(editRequest.getDescription());
            map.setImageUrl(editRequest.getImageUrl());
            Map newMap = mapService.save(map);
            return ResponseEntity.ok(newMap);
            }
            return ResponseEntity.status(403).body(new AuthResponse(null, null, "Invalid credentials"));
        }
        return ResponseEntity.status(404).body(new AuthResponse(null, null, "Invalid map or project id"));
    }
}
