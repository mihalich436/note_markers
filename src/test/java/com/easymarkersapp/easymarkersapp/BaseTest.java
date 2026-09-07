package com.easymarkersapp.easymarkersapp;

import com.easymarkersapp.easymarkersapp.model.User;
import com.easymarkersapp.easymarkersapp.repository.ProjectAccessRepository;
import com.easymarkersapp.easymarkersapp.repository.ProjectRepository;
import com.easymarkersapp.easymarkersapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected ProjectRepository projectRepository;
    @Autowired
    protected ProjectAccessRepository accessRepository;

    protected final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    protected String testToken;
    protected User testUser;

    @BeforeEach
    void setUpBase() {
        accessRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash(passwordEncoder.encode("password123"));
        testUser = userRepository.save(testUser);
    }
}
