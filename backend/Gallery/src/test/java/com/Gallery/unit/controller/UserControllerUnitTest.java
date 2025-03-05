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
        final User result = userController.createUser(dtoE);

        verify(userService).createUser(dtoE);
        assertEquals(e, result);
    }

}
