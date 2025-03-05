package com.Gallery.integration.controller;

import com.Gallery.model.User;
import com.Gallery.repository.UserRepository;
import com.Gallery.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private UserServiceImpl userService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final User e = new User();

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
        setupUser();
    }

    private void setupUser() {
        e.setUsername("Test");
        e.setEmail("test@example.com");
        e.setPassword("securePassword");
    }

    @Test
    @WithMockUser
    public void shouldReturnCreatedWhenCreatingWithNoConflicts() throws Exception {
        String userJson = objectMapper.writeValueAsString(e);

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(e.getUsername())))
                .andExpect(jsonPath("$.email", is(e.getEmail())))
                .andDo(print())
        ;
    }

    @Test
    @WithMockUser
    public void shouldReturnOKWhenGetWithIdExists() throws Exception {
        userRepository.save(e);
        mockMvc.perform(get("/api/users/{id}", e.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(e.getUsername())))
                .andExpect(jsonPath("$.email", is(e.getEmail())))
                .andExpect(jsonPath("$.password", is(e.getPassword())))
                .andDo(print())
        ;
    }

    @Test
    public void shouldReturnOKWhenSettingAuthTokenUib() throws Exception {
        userRepository.save(e);
        e.setUibToken("secureToken");

        mockMvc.perform(put("/api/users/{id}/setUibToken", e.getId())
                        .with(csrf())
                        .content(e.getUibToken())
                        .with(user(e)))
                .andExpect(status().isCreated())
                .andDo(print())
        ;

        User updatedUser = userRepository.findById(e.getId()).get();
        assertEquals(e.getUibToken(), updatedUser.getUibToken());
    }

    @Test
    public void shouldReturnOKWhenSettingAuthTokenHvl() throws Exception {
        userRepository.save(e);
        e.setHvlToken("secureToken");

        mockMvc.perform(put("/api/users/{id}/setHvlToken", e.getId())
                        .with(csrf())
                        .content(e.getHvlToken())
                        .with(user(e)))
                .andExpect(status().isCreated())
                .andDo(print())
        ;

        User updatedUser = userRepository.findById(e.getId()).get();
        assertEquals(e.getHvlToken(), updatedUser.getHvlToken());
    }
}
