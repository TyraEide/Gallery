package com.Gallery.unit.controller;

import com.Gallery.controller.UserController;
import com.Gallery.dto.UserRegistrationDTO;
import com.Gallery.mapper.UserRegistrationMapper;
import com.Gallery.model.User;
import com.Gallery.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {
    @Mock private UserService userService;
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
        User e = new User("testuser", "email@uib.no", "SecurePass123!");
        UserRegistrationDTO dtoE = urMapper.toDTO(e);
        when(userService.createUser(dtoE)).thenReturn(e);
        final ResponseEntity<?> responseEntity = userController.createUser(dtoE);
        verify(userService).createUser(dtoE);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(e, responseEntity.getBody());
    }


    @Test
    public void shouldFailWhenEmailIsInvalid() {
        User e = new User("testuser", "invalidEmail", "SecurePass123!");
        UserRegistrationDTO dtoE = urMapper.toDTO(e);
        ResponseEntity<?> responseEntity = userController.createUser(dtoE);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void shouldBeCreatedWhenEmailIsPlusAdressed() {
        User e = new User("testuser", "email+java@example.com", "SecurePass123!");
        UserRegistrationDTO dtoE = urMapper.toDTO(e);
        ResponseEntity<?> responseEntity = userController.createUser(dtoE);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void shouldFailWhenPasswordIsWeak() {
        User e = new User("testuser", "test@example.com", "123");
        UserRegistrationDTO dtoE = urMapper.toDTO(e);
        ResponseEntity<?> responseEntity = userController.createUser(dtoE);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void shouldFailWhenUsernameAreEmpty() {
        User e = new User("", "example@example.com", "1deefefefffe!");
        UserRegistrationDTO dtoE = urMapper.toDTO(e);
        ResponseEntity<?> responseEntity = userController.createUser(dtoE);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    @Test
    public void shouldFailWhenEmptyEmail() {
        User e = new User("aUserName", "", "1deefefefffe!");
        UserRegistrationDTO dtoE = urMapper.toDTO(e);
        ResponseEntity<?> responseEntity = userController.createUser(dtoE);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void shouldFailWhenEmptyPassword() {
        User e = new User("aUserName", "example@example.com", "");
        UserRegistrationDTO dtoE = urMapper.toDTO(e);
        ResponseEntity<?> responseEntity = userController.createUser(dtoE);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void shouldFailWhenUsernameIsTooShort() {
        User e = new User("t", "example@example.com", "234234sdf234s!");
        UserRegistrationDTO dtoE = urMapper.toDTO(e);
        ResponseEntity<?> responseEntity = userController.createUser(dtoE);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }


}
