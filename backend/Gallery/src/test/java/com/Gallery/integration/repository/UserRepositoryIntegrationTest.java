package com.Gallery.integration.repository;

import com.Gallery.model.User;
import com.Gallery.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryIntegrationTest {
    @Autowired UserRepository userRepository;
    @Autowired TestEntityManager testEntityManager;
    @Autowired JdbcTemplate jdbcTemplate;

    User e;

    @BeforeEach
    public void setupTestUser() {
        e = new User();
        e.setUsername("Test");
        e.setEmail("test@example.com");
        e.setPassword("securePassword");
    }

    @AfterEach
    public void emptyDatabase() {
        userRepository.deleteAll();
    }

    @Test
    public void shouldSetIdToUserWhenSaving() {
        User inserted = userRepository.save(e);
        assertNotNull(inserted);
        assertEquals(testEntityManager.find(User.class, inserted.getId()), e);
    }

//    @Test
//    public void shouldEncryptTokensWhenSaving() {
//        String uibToken = "uibToken";
//        String hvlToken = "hvlToken";
//
////        e.setUibToken(uibToken);
////        e.setHvlToken(hvlToken);
//        userRepository.saveAndFlush(e);
//
//        String sqlUib = "SELECT uib_token FROM users WHERE id='%s'".formatted(e.getId());
//        String secretUibTokenInDB = jdbcTemplate.queryForObject(sqlUib, String.class);
//        assertNotEquals(uibToken, secretUibTokenInDB);
//
//        String sqlHvl = "SELECT hvl_token FROM users WHERE id='%s'".formatted(e.getId());
//        String secretHvlTokenInDB = jdbcTemplate.queryForObject(sqlHvl, String.class);
//        assertNotEquals(hvlToken, secretHvlTokenInDB);
//
//        User inserted = userRepository.findById(e.getId()).get();
////        assertEquals(uibToken, inserted.getUibToken());
////        assertEquals(hvlToken, inserted.getHvlToken());
//    }
}
