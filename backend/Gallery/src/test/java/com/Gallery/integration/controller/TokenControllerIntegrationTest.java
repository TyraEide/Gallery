package com.Gallery.integration.controller;

import com.Gallery.controller.TokenController;
import com.Gallery.dto.UserRegistrationDTO;
import com.Gallery.model.CanvasToken;
import com.Gallery.model.Institution;
import com.Gallery.model.User;
import com.Gallery.service.InstitutionService;
import com.Gallery.service.TokenService;
import com.Gallery.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TokenControllerIntegrationTest {
    @Autowired TokenService tokenService;
    @Autowired UserService userService;
    @Autowired InstitutionService institutionService;
    @Autowired TokenController tokenController;
    @Autowired MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();
    private CanvasToken token;
    private User testUser;
    private Institution testInstitution;

    @BeforeEach
    public void initializeTests() {
        wipeDatabase();
        setupTestUser();
        setupTestInstitution();
    }

    private void wipeDatabase() {
        userService.deleteAll();
        institutionService.deleteAll();
        tokenService.deleteAll();
    }

    private void setupTestUser() {
        UserRegistrationDTO e = new UserRegistrationDTO();
        e.setUsername("Test");
        e.setEmail("test@example.com");
        e.setPassword("securePassword");
        testUser = userService.createUser(e);
    }

    private void setupTestInstitution() {
        Institution e = new Institution();
        e.setApiUrl("https://mitt.uib.no/api/v1");
        e.setShortName("uib");
        e.setFullName("The University of Bergen");
        testInstitution = institutionService.create(e);
    }

    private CanvasToken createTestToken(User user, Institution institution) {
        token = new CanvasToken();
        token.setToken("token");
        token.setUser(user);
        token.setInstitution(institution);
        return token;
    }

    @Test
    public void shouldReturnOKWhenSettingAuthToken() throws Exception {
        CanvasToken token = createTestToken(testUser, testInstitution);
        String tokenJson = mapper.writeValueAsString(token);

        mockMvc.perform(post("/api/tokens")
                        .with(csrf())
                        .with(user(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tokenJson))
                .andExpect(status().isCreated())
                .andDo(print())
        ;
    }

}
