package com.Gallery.integration.repository;

import com.Gallery.dto.UserRegistrationDTO;
import com.Gallery.model.CanvasToken;
import com.Gallery.model.Institution;
import com.Gallery.model.User;
import com.Gallery.repository.InstitutionRepository;
import com.Gallery.repository.TokenRepository;
import com.Gallery.repository.UserRepository;
import com.Gallery.service.InstitutionService;
import com.Gallery.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TokenRepositoryIntegrationTest {
    @Autowired TokenRepository tokenRepository;
    @Autowired UserRepository userService;
    @Autowired InstitutionRepository institutionService;
    @Autowired TestEntityManager testEntityManager;
    @Autowired JdbcTemplate jdbcTemplate;

    private User savedUser;
    private Institution savedInstitution;

    @BeforeEach
    public void initializeTests() {
        wipeDatabase();
        setupTestUser();
        setupTestInstitution();
    }

    private void wipeDatabase() {
        userService.deleteAll();
        institutionService.deleteAll();
        tokenRepository.deleteAll();
    }

    private void setupTestUser() {
        User e = new User();
        e.setUsername("Test");
        e.setEmail("test@example.com");
        e.setPassword("securePassword");
        savedUser = userService.save(e);
    }

    private void setupTestInstitution() {
        Institution e = new Institution();
        e.setApiUrl("https://mitt.uib.no/api/v1");
        e.setShortName("uib");
        e.setFullName("The University of Bergen");
        savedInstitution = institutionService.save(e);
    }

    private CanvasToken createTestToken(User user, Institution institution) {
        CanvasToken token = new CanvasToken();
        token.setToken("token");
        token.setUser(user);
        token.setInstitution(institution);
        return token;
    }

    @Test
    public void shouldEncryptTokensWhenSaving() {
        CanvasToken token = createTestToken(savedUser, savedInstitution);

        tokenRepository.saveAndFlush(token);

        String sqlToken = "SELECT token FROM canvas_tokens WHERE user_id='%s'".formatted(savedUser.getId());
        String secretTokenInDB = jdbcTemplate.queryForObject(sqlToken, String.class);
        assertNotEquals(token.getToken(), secretTokenInDB);
    }
}
