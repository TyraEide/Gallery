package com.Gallery.unit.model;

import com.Gallery.converter.StringCryptoConverter;
import com.Gallery.model.User;
import com.ulisesbocchio.jasyptspringboot.configuration.EnableEncryptablePropertiesConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@ExtendWith(SpringExtension.class)
public class UserUnitTest {

    @Test
    public void shouldReturnSetArgumentsUponGet() {
        String username = "test";
        String email = "test@example.com";
        String password = "verySecurePassword";

        User e = new User();
        e.setUsername(username);
        e.setEmail(email);
        e.setPassword(password);

        assertEquals(username, e.getUsername());
        assertEquals(email, e.getEmail());
        assertEquals(password, e.getPassword());
    }
}
