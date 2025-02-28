package com.Gallery.integration.service;

import com.Gallery.model.User;
import com.Gallery.repository.UserRepository;
import com.Gallery.service.UserService;
import com.Gallery.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceIntegrationTest {
    @MockitoBean UserRepository userRepository;

    @Autowired UserService userService;

    @Test
    public void shouldNotStorePasswordInPlainText() {
        String password = "password";
        User e = new User("test", "test@example.com", password);

        when(userRepository.save(e)).thenReturn(e);
        when(userRepository.findById(e.getId())).thenReturn(Optional.of(e));
        User inserted = userService.createUser(e);

        String loadedPassword = userRepository.findById(inserted.getId()).get().getPassword();
        assertNotEquals(password, loadedPassword);
    }
}
