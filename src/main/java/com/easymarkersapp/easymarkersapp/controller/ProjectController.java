package com.easymarkersapp.easymarkersapp.controller;

import com.easymarkersapp.easymarkersapp.dto.*;
import com.easymarkersapp.easymarkersapp.model.*;
import com.easymarkersapp.easymarkersapp.service.MapService;
import com.easymarkersapp.easymarkersapp.service.ProjectAccessService;
import com.easymarkersapp.easymarkersapp.service.ProjectService;
import com.easymarkersapp.easymarkersapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin() //> Указать адрес и порт фронта
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectAccessService accessService;
    @Autowired
    private UserService userService;
    @Autowired
    private MapService mapService;

    @GetMapping
    public ResponseEntity<?> getProjects() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        List<Project> projects = projectService.findByOwnerId(currentUser.getId());

//        List<ProjectAccess> projectAccesses = accessService.findByUser(currentUser);
//        List<Project> projects = projectAccesses.stream().map(ProjectAccess::getProject).collect(Collectors.toList());

        List<Project> projects = projectService.findByUser(currentUser);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProject(@PathVariable Long id) {
//        Optional<Project> projectOptional = projectService.findById(id);
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Optional<ProjectAccess> projectOptional = accessService.findByProjectIdAndUser(id, currentUser);
        Project project = projectService.findByProjectIdAndUser(id, currentUser);

        if (project != null) {
//            Project project = projectOptional.get().getProject();
            System.out.println(project);
//            if (currentUser.projectBelongUser(project)){
            return ResponseEntity.ok(project);
//            }
//            return ResponseEntity.status(403).body(new AuthResponse(null, null, "Invalid credentials"));
        }
        return ResponseEntity.status(404).body(new AuthResponse(null, null, "Cannot access project"));
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

    @GetMapping("/{projectId}/maps") //> fix for shared access
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

    @PostMapping("/{projectId}/maps") //> fix for shared access (admin can create map)
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

    @PutMapping("/{projectId}/maps/{mapId}") //> fix for shared access (admin can edit map)
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

    // ========== УПРАВЛЕНИЕ ДОСТУПОМ ==========

    /**
     * Получить всех пользователей с доступом к проекту (включая владельца)
     * GET /api/projects/{id}/access
     */
    @GetMapping("/{id}/access")
    public ResponseEntity<?> getProjectAccess(@PathVariable Long id,
                                              @AuthenticationPrincipal User currentUser) {
       Project project = projectService.findById(id).orElse(null);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        // Проверяем доступ (владелец или администратор)
        if (!project.getOwnerId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).body("Нет прав на управление доступом");
        }

        List<AccessUserDTO> usersWithAccess = new ArrayList<>();

        // Добавляем владельца
        usersWithAccess.add(new AccessUserDTO(
                project.getOwner().getId(),
                project.getOwner().getUsername(),
                project.getOwner().getEmail(),
                "OWNER",
                "Владелец",
                true
        ));

        // Добавляем пользователей с доступом
        List<ProjectAccess> accesses = accessService.findByProject(project);
        usersWithAccess.addAll(accesses.stream()
                .map(access -> new AccessUserDTO(
                        access.getUser().getId(),
                        access.getUser().getUsername(),
                        access.getUser().getEmail(),
                        access.getRole().name(),
                        access.getRole().getDisplayName(),
                        false
                )).toList());

        return ResponseEntity.ok(usersWithAccess);
    }

    /**
     * Добавить пользователя в проект (по email)
     * POST /api/projects/{id}/access
     */
    @PostMapping("/{id}/access")
    public ResponseEntity<?> addUserAccess(@PathVariable Long id,
                                           @RequestBody ShareRequest request,
                                           @AuthenticationPrincipal User currentUser) {
        Project project = projectService.findById(id).orElse(null);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        if (!currentUser.projectBelongUser(project)) {
            return ResponseEntity.status(403).body("Только владелец может добавлять пользователей");
        }

        // Проверяем email
        String email = request.getEmail().trim();
        if (email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email не может быть пустым");
        }

        // Проверяем роль
        AccessRole role;
        try {
            role = AccessRole.valueOf(request.getRole());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Неверная роль. Доступные роли: READ_ONLY, CHAT, EDITOR, ADMIN");
        }

        // Ищем пользователя по email (создаем если не существует)
        User userToAdd = userService.findByEmail(email).orElse(null);

        if (userToAdd == null) {
            userToAdd = new User();
            userToAdd.setUsername(email.split("@")[0]);
            userToAdd.setEmail(email);
            userToAdd.setPasswordHash("");
            userToAdd = userService.save(userToAdd);
//            return ResponseEntity.badRequest().body("Пользователь не зарегистрирован");
        }

        // Проверяем, не является ли пользователь владельцем
        if (project.getOwnerId().equals(userToAdd.getId())) {
            return ResponseEntity.badRequest().body("Нельзя добавить владельца проекта");
        }

        // Проверяем, не добавлен ли уже пользователь
        if (accessService.existsByProjectAndUser(project, userToAdd)) {
            return ResponseEntity.badRequest().body("Пользователь уже имеет доступ к проекту");
        }

        // Создаем доступ
        ProjectAccess access = new ProjectAccess(project, userToAdd, role, request.getNickname().trim());
        accessService.save(access);

        return ResponseEntity.ok().body("Пользователь добавлен в проект с ролью: " + role.getDisplayName());
    }

    /**
     * Изменить роль пользователя
     * PUT /api/projects/{id}/access/{userId}
     */
    @PutMapping("/{id}/access/{userId}")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id,
                                            @PathVariable Long userId,
                                            @RequestBody UpdateRoleRequest request,
                                            @AuthenticationPrincipal User currentUser) {
        Project project = projectService.findById(id).orElse(null);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        if (!currentUser.projectBelongUser(project)) {
            return ResponseEntity.status(403).body("Только владелец может изменять роли");
        }

        // Проверяем роль
        AccessRole newRole;
        try {
            newRole = AccessRole.valueOf(request.getRole());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Неверная роль. Доступные роли: READ_ONLY, CHAT, EDITOR, ADMIN");
        }

        // Находим доступ пользователя
        User userToUpdate = userService.findById(userId).orElse(null);
        if (userToUpdate == null) {
            return ResponseEntity.status(404).body("Пользователь не найден");
        }

        // Нельзя менять роль владельца
        if (project.getOwnerId().equals(userId)) {
            return ResponseEntity.badRequest().body("Нельзя изменить роль владельца");
        }

        ProjectAccess access = accessService.findByProjectAndUser(project, userToUpdate)
                .orElse(null);

        if (access == null) {
            return ResponseEntity.status(404).body("Пользователь не имеет доступа к проекту");
        }

        // Обновляем роль
        access.setRole(newRole);
        access.setNickname(request.getNickname());
        accessService.save(access);

        return ResponseEntity.ok().body("Роль изменена на: " + newRole.getDisplayName());
    }

    /**
     * Удалить пользователя из проекта
     * DELETE /api/projects/{id}/access/{userId}
     */
    @DeleteMapping("/{id}/access/{userId}")
    public ResponseEntity<?> removeUserAccess(@PathVariable Long id,
                                              @PathVariable Long userId,
                                              @AuthenticationPrincipal User currentUser) {
        Project project = projectService.findById(id).orElse(null);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        if (!currentUser.projectBelongUser(project)) {
            return ResponseEntity.status(403).body("Только владелец может удалять пользователей");
        }

        // Нельзя удалить владельца
        if (project.getOwnerId().equals(userId)) {
            return ResponseEntity.badRequest().body("Нельзя удалить владельца проекта");
        }

//        User userToRemove = userService.findById(userId).orElse(null);
//        if (userToRemove == null) {
//            return ResponseEntity.notFound().body("Пользователь не найден");
//        }
        User userToRemove = new User();
        userToRemove.setId(userId);

        // Удаляем доступ
        accessService.deleteByProjectAndUser(project, userToRemove);

        return ResponseEntity.ok().body("Пользователь удален из проекта");
    }
}
