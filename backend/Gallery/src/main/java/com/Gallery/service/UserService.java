package com.Gallery.service;

import com.Gallery.dto.UserRegistrationDTO;
import com.Gallery.mapper.UserRegistrationMapper;
import com.Gallery.model.User;
import com.Gallery.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final UserRegistrationMapper urMapper = new UserRegistrationMapper();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserRegistrationDTO userDTO) {
        User user = urMapper.toEntity(userDTO);
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(UUID userId, User user) {
        User existingUser = getUser(userId);
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(encoder.encode(user.getPassword()));
        existingUser.setEmail(user.getEmail());
        existingUser.setUibToken(user.getUibToken());
        existingUser.setHvlToken(user.getHvlToken());
        return userRepository.save(user);
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).orElse(null);
    }


    private User setTokenValidation(UUID userId, User auth) {
        User user = this.getUser(userId);
        // User not found
        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        // Unauthorized
        if (auth.equals(user)) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        return getUser(userId);
    }

    public User setUibToken(String token, UUID userId, User auth) {
        User user = setTokenValidation(userId, auth);
        user.setUibToken(token);
        return updateUser(userId, user);
    }

    public User setHvlToken(String token, UUID userId, User auth) {
        User user = setTokenValidation(userId, auth);
        user.setHvlToken(token);
        return updateUser(userId, user);
    }


}
