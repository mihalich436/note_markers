package com.easymarkersapp.easymarkersapp;

import com.easymarkersapp.easymarkersapp.dto.LoginRequest;
import com.easymarkersapp.easymarkersapp.model.AccessRole;
import com.easymarkersapp.easymarkersapp.model.Project;
import com.easymarkersapp.easymarkersapp.model.ProjectAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProjectControllerTest extends BaseTest {
    private String authToken;
    private Project testProject;

    @BeforeEach
    void setUp() throws Exception {
        // Получаем токен для тестового пользователя
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        var result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        authToken = objectMapper.readTree(response).get("token").asString();

        // Создаем тестовый проект
        testProject = new Project();
        testProject.setTitle("Test Project");
        testProject.setDescription("Test Description");
        testProject.setOwnerId(testUser.getId());
        testProject.setOwner(testUser);
        testProject = projectRepository.save(testProject);
        ProjectAccess projectAccess = new ProjectAccess(testProject, testUser, AccessRole.ADMIN, "Owner");
        accessRepository.save(projectAccess);
    }

    @Test
    void getUserProjects_ShouldReturnUserProjects() throws Exception {
        mockMvc.perform(get("/api/projects")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Test Project"))
                .andExpect(jsonPath("$[0].description").value("Test Description"))
                .andExpect(jsonPath("$[0].owner").value(true));
    }

    @Test
    void getUserProjects_WithoutToken_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createProject_ShouldCreateNewProject() throws Exception {
        Map<String, String> projectRequest = new HashMap<>();
        projectRequest.put("title", "New Project");
        projectRequest.put("description", "New Description");

        mockMvc.perform(post("/api/projects")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Project"))
                .andExpect(jsonPath("$.description").value("New Description"));
    }

    @Test
    void getProjectById_WithValidId_ShouldReturnProject() throws Exception {
        mockMvc.perform(get("/api/projects/" + testProject.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.project.title").value("Test Project"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void getProjectById_WithInvalidId_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/projects/99999")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProject_ShouldUpdateExistingProject() throws Exception {
        Map<String, String> updateRequest = new HashMap<>();
        updateRequest.put("title", "Updated Title");
        updateRequest.put("description", "Updated Description");

        mockMvc.perform(put("/api/projects/" + testProject.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    void deleteProject_ShouldRemoveProject() throws Exception {
        mockMvc.perform(delete("/api/projects/" + testProject.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk());

        // Проверяем, что проект удален
        mockMvc.perform(get("/api/projects/" + testProject.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }
}
