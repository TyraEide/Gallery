package com.Gallery.unit.service;

import com.Gallery.dto.UserRegistrationDTO;
import com.Gallery.mapper.UserRegistrationMapper;
import com.Gallery.model.User;
import com.Gallery.repository.UserRepository;
import com.Gallery.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserService}.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private UserService userService;
    private final UserRegistrationMapper urMapper = new UserRegistrationMapper();

    @Test
    public void shouldReturnUserByIdWhenPresentFromRepository() {
        User e = new User();
        when(userRepository.findById(e.getId())).thenReturn(Optional.of(e));
        final User result = userService.getUser(e.getId());

        verify(userRepository).findById(e.getId());
        assertEquals(e, result);
    }

    @Test
    public void shouldCreateUserThroughRepository() {
        User e = new User();
        e.setUsername("test");
        e.setEmail("test@example.com");
        e.setPassword("password123!");
        when(userRepository.save(e)).thenReturn(e);
        final User result = userService.createUser(urMapper.toDTO(e));

        verify(userRepository).save(e);
        assertEquals(e, result);
    }


}
