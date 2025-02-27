package com.Gallery.integration.controller;

import com.Gallery.controller.UserController;
import com.Gallery.model.User;
import com.Gallery.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class UserControllerIntegrationTest {
    @Autowired MockMvc mockMvc;
    @MockitoBean
    private UserServiceImpl userService;

    @Autowired
    private UserController userController;
    @Test
    @WithMockUser
    public void shouldReturnUserUponCreation() throws Exception {
        User e = new User("Test", "test@example.com", "securePassword");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(e);

        when(userService.createUser(e)).thenReturn(e);

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(e.getUsername())))
                .andExpect(jsonPath("$.email", is(e.getEmail())))
                .andExpect(jsonPath("$.password", is(e.getPassword())))
        ;
    }

    @Test
    @WithMockUser
    public void shouldReturnUserUponGetWithId() throws Exception {
        User e = mock(User.class);
        e.setUsername("Test");
        e.setEmail("test@example.com");
        e.setPassword("securePassword");
        when(e.getId()).thenReturn(UUID.fromString("0000-00-00-00-000000"));

        when(userService.createUser(e)).thenReturn(e);
        final User se = userController.createUser(e);
        when(userService.getUser(e.getId())).thenReturn(e);

        mockMvc.perform(get("/api/users/{id}", se.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(se.getUsername())))
                .andExpect(jsonPath("$.email", is(se.getEmail())))
                .andExpect(jsonPath("$.password", is(se.getPassword())))
        ;
    }
}
