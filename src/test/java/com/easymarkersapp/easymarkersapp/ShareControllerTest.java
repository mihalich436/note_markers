package com.easymarkersapp.easymarkersapp;

import com.easymarkersapp.easymarkersapp.dto.LoginRequest;
import com.easymarkersapp.easymarkersapp.model.AccessRole;
import com.easymarkersapp.easymarkersapp.model.Project;
import com.easymarkersapp.easymarkersapp.model.ProjectAccess;
import com.easymarkersapp.easymarkersapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ShareControllerTest extends BaseTest {
    private String ownerToken;
    private User collaborator;
    private Project testProject;

    @BeforeEach
    void setUp() throws Exception {
        // Создаем второго пользователя
        collaborator = new User();
        collaborator.setUsername("collaborator");
        collaborator.setEmail("collab@example.com");
        collaborator.setPasswordHash(passwordEncoder.encode("password123"));
        collaborator = userRepository.save(collaborator);

        // Получаем токен владельца
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        var result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ownerToken = objectMapper.readTree(response).get("token").asString();

        // Создаем тестовый проект
        testProject = new Project();
        testProject.setTitle("Shared Project");
        testProject.setDescription("Test Description");
        testProject.setOwnerId(testUser.getId());
        testProject.setOwner(testUser);
        testProject = projectRepository.save(testProject);
        ProjectAccess projectAccess = new ProjectAccess(testProject, testUser, AccessRole.ADMIN, "Owner");
        accessRepository.save(projectAccess);
    }

    @Test
    void getProjectAccess_ShouldReturnAllUsersWithAccess() throws Exception {
        // Добавляем collaborator в доступ
        ProjectAccess access = new ProjectAccess(testProject, collaborator, AccessRole.EDITOR, "Editor");
        accessRepository.save(access);

        mockMvc.perform(get("/api/projects/" + testProject.getId() + "/access")
                        .header("Authorization", "Bearer " + ownerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.role == 'ADMIN')]").exists())
                .andExpect(jsonPath("$[?(@.email == 'collab@example.com')]").exists());
    }

    @Test
    void addUserAccess_ShouldAddUserToProject() throws Exception {
        Map<String, String> shareRequest = new HashMap<>();
        shareRequest.put("email", "collab@example.com");
        shareRequest.put("role", "EDITOR");
        shareRequest.put("nickname", "Colab");

        mockMvc.perform(post("/api/projects/" + testProject.getId() + "/access")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shareRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь добавлен в проект с ролью: Редактирование"));
    }

    @Test
    void addUserAccess_WithNonExistentEmail_ShouldCreateTemporaryUser() throws Exception {
        Map<String, String> shareRequest = new HashMap<>();
        shareRequest.put("email", "newuser@example.com");
        shareRequest.put("role", "READ_ONLY");

        mockMvc.perform(post("/api/projects/" + testProject.getId() + "/access")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shareRequest)))
                .andExpect(status().isOk());

        // Проверяем, что пользователь создан
        var createdUser = userRepository.findByEmailIgnoreCase("newuser@example.com");
        assert createdUser.isPresent();
    }

    @Test
    void updateUserRole_ShouldChangeUserRole() throws Exception {
        // Сначала добавляем пользователя
        ProjectAccess access = new ProjectAccess(testProject, collaborator, AccessRole.READ_ONLY, "Colab");
        accessRepository.save(access);

        Map<String, String> roleRequest = new HashMap<>();
        roleRequest.put("role", "ADMIN");

        mockMvc.perform(put("/api/projects/" + testProject.getId() + "/access/" + collaborator.getId())
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Роль изменена на: Мастер"));
    }

    @Test
    void removeUserAccess_ShouldRemoveUserFromProject() throws Exception {
        // Сначала добавляем пользователя
        ProjectAccess access = new ProjectAccess(testProject, collaborator, AccessRole.EDITOR, "Colab");
        accessRepository.save(access);

        mockMvc.perform(delete("/api/projects/" + testProject.getId() + "/access/" + collaborator.getId())
                        .header("Authorization", "Bearer " + ownerToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь удален из проекта"));

        // Проверяем, что доступ удален
        var deletedAccess = accessRepository.findByProjectAndUser(testProject, collaborator);
        assert deletedAccess.isEmpty();
    }

    @Test
    void nonOwner_ShouldNotBeAbleToAddUsers() throws Exception {
        // Получаем токен collaborator
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "collab@example.com");
        loginRequest.put("password", "password123");

        var result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String collaboratorToken = objectMapper.readTree(response).get("token").asString();

        Map<String, String> shareRequest = new HashMap<>();
        shareRequest.put("email", "someone@example.com");
        shareRequest.put("role", "READ_ONLY");

        mockMvc.perform(post("/api/projects/" + testProject.getId() + "/access")
                        .header("Authorization", "Bearer " + collaboratorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shareRequest)))
                .andExpect(status().isForbidden());
    }
}