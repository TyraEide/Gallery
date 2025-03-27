package com.Gallery.integration.service;

import com.Gallery.mapper.UserRegistrationMapper;
import com.Gallery.model.User;
import com.Gallery.repository.UserRepository;
import com.Gallery.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class UserServiceIntegrationTest {
    @Autowired UserRepository userRepository;

    @Autowired UserService userService;
    private final UserRegistrationMapper urMapper = new UserRegistrationMapper();

    @AfterEach
    public void emptyDatabase() {
        userRepository.deleteAll();
    }

    @Test
    public void shouldNotStorePasswordInPlainText() {
        String password = "password";
        User e = new User("test", "test@example.com", password);

        User inserted = userService.createUser(urMapper.toDTO(e));
        String loadedPassword = userRepository.findById(inserted.getId()).get().getPassword();
        assertNotEquals(password, loadedPassword);
    }
}
