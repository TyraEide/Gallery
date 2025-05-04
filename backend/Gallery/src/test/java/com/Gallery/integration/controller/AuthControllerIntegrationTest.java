package com.Gallery.integration.controller;

import com.Gallery.model.User;
import com.Gallery.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired private UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private User e;

    @BeforeEach
    public void initializeTests() {
        wipeDatabase();
        setupTestUser();
    }

    private void setupTestUser() {
        e = new User();
        e.setUsername("Test");
        e.setEmail("test@example.com");
        e.setPassword("securePassword1!");
    }

    private void wipeDatabase() {
        userService.deleteAll();
    }
}
