package com.Gallery.service.impl;

import com.Gallery.dto.UserRegistrationDTO;
import com.Gallery.mapper.UserRegistrationMapper;
import com.Gallery.model.User;
import com.Gallery.repository.UserRepository;
import com.Gallery.service.UserService;
import com.Gallery.utilities.EmailValidator;
import com.Gallery.utilities.PasswordValidator;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final UserRegistrationMapper urMapper = new UserRegistrationMapper();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserRegistrationDTO userDTO) throws NullPointerException {

        if(userDTO.getUsername() == null || userDTO.getUsername().isBlank()) {
            throw new NullPointerException("Username is required");
        }

        if(userDTO.getPassword() == null || userDTO.getPassword().isBlank()) {
            throw new NullPointerException("Password is required");
        }

        if(userDTO.getEmail() == null || userDTO.getEmail().isBlank()) {
            throw new NullPointerException("Email is required");
        }

        if(!EmailValidator.validateEmail(userDTO.getEmail())){
            throw new IllegalArgumentException("Invalid email format");
        }

        if(!PasswordValidator.validatePassword(userDTO.getPassword())){
            throw new IllegalArgumentException("Password must contain letters, numbers, and symbols");
        }

        User user = urMapper.toEntity(userDTO);
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UUID userId, User user) {
        User existingUser = getUser(userId);
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(encoder.encode(user.getPassword()));
        existingUser.setEmail(user.getEmail());
        existingUser.setUibToken(user.getUibToken());
        existingUser.setHvlToken(user.getHvlToken());
        return userRepository.save(user);
    }

    @Override
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

    @Override
    public User setUibToken(String token, UUID userId, User auth) {
        User user = setTokenValidation(userId, auth);
        user.setUibToken(token);
        return updateUser(userId, user);
    }

    @Override
    public User setHvlToken(String token, UUID userId, User auth) {
        User user = setTokenValidation(userId, auth);
        user.setHvlToken(token);
        return updateUser(userId, user);
    }


}
