package com.Gallery.unit.controller;

import com.Gallery.controller.UserController;
import com.Gallery.dto.UserRegistrationDTO;
import com.Gallery.mapper.UserRegistrationMapper;
import com.Gallery.model.User;
import com.Gallery.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {
    @Mock private UserServiceImpl userService;
    @InjectMocks private UserController userController;
    private UserRegistrationMapper urMapper = new UserRegistrationMapper();

    @Test
    public void shouldReturnUserByIdWhenPresentFromService() {
        User e = new User();
        when(userService.getUser(e.getId())).thenReturn(e);
        final User result = userController.getUser(e.getId());

        verify(userService).getUser(e.getId());
        assertEquals(e, result);
    }

    @Test
    public void shouldCreateUserThroughService() {
        User e = new User();
        UserRegistrationDTO dtoE = urMapper.toDTO(e);
        when(userService.createUser(dtoE)).thenReturn(e);

        // Change the assertion to work with ResponseEntity
        final ResponseEntity<?> responseEntity = userController.registerUser(dtoE);

        // Verify the service was called
        verify(userService).createUser(dtoE);

        // Check if the status is 201 Created and the response body contains the correct user
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(e, responseEntity.getBody());
    }

}
