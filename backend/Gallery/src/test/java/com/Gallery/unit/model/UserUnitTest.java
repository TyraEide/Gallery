package com.Gallery.unit.model;

import com.Gallery.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserUnitTest {

    @Test
    public void shouldReturnSetArgumentsUponGet() {
        String username = "test";
        String email = "test@example.com";
        String password = "verySecurePassword";
        String uibToken = "uibToken";
        String hvlToken = "hvlToken";

        User e = new User();
        e.setUsername(username);
        e.setEmail(email);
        e.setPassword(password);
        e.setUibToken(uibToken);
        e.setHvlToken(hvlToken);

        assertEquals(username, e.getUsername());
        assertEquals(email, e.getEmail());
        assertEquals(password, e.getPassword());
        assertEquals(uibToken, e.getUibToken());
        assertEquals(hvlToken, e.getHvlToken());
    }
}
