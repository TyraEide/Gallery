package com.Gallery.unit.service;

import com.Gallery.model.User;
import com.Gallery.repository.UserRepository;
import com.Gallery.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock private UserRepository userRepository;

    @InjectMocks private UserServiceImpl userService;

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
        when(userRepository.save(e)).thenReturn(e);
        final User result = userService.createUser(e);

        verify(userRepository).save(e);
        assertEquals(e, result);
    }
}
