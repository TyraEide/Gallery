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
        return userRepository.save(user);
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND,
                "No user with id " + id + " found."));
    }

    public boolean existsById(UUID userId) {
        return userRepository.existsById(userId);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }


}
