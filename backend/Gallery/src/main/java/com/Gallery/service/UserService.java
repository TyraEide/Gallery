package com.Gallery.service;

import com.Gallery.dto.UserRegistrationDTO;
import com.Gallery.mapper.UserRegistrationMapper;
import com.Gallery.model.User;
import com.Gallery.repository.UserRepository;
import jakarta.annotation.security.RunAs;
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

        User user = urMapper.toEntity(userDTO);
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(UUID userId, User user) {
        User existingUser = getUser(userId);
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(encoder.encode(user.getPassword()));
        existingUser.setEmail(user.getEmail());
        return userRepository.save(user);
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND,
                "No user with id " + id + " found."));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND,
                "No user with email " + email + " found."));
    }

    public boolean existsById(UUID userId) {
        return userRepository.existsById(userId);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }


}
