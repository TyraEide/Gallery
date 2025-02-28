package com.Gallery.integration.repository;

import com.Gallery.model.User;
import com.Gallery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
public class UserRepositoryIntegrationTest {
    @Autowired UserRepository userRepository;
    @Autowired TestEntityManager testEntityManager;
    @Autowired JdbcTemplate jdbcTemplate;

    User e;

    @BeforeEach
    public void initializerUser() {
        e = new User("test", "test@example.com", "superSecretPassword");
    }

    @Test
    public void shouldSetIdToUserWhenSaving() {
        e.setCanvasAuthToken("verySecretToken");
        User inserted = userRepository.save(e);
        assertEquals(testEntityManager.find(User.class, inserted.getId()), e);
    }

    @Test
    public void shouldEncryptCanvasAuthTokenWhenSaving() {
        String token = "verySecretToken";

        User e = new User("test", "test@example.com", "superSecretPassword");
        e.setCanvasAuthToken(token);

        User inserted = userRepository.saveAndFlush(e);

        String sql = "SELECT canvas_auth_token FROM users WHERE id='%s'".formatted(inserted.getId());
        String secretTokenInDB = jdbcTemplate.queryForObject(sql, String.class);
        assertNotEquals(token, secretTokenInDB);
        String loadedToken = userRepository.findById(inserted.getId()).get().getCanvasAuthToken();
        assertEquals(token, loadedToken);
    }
}
