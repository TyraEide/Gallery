package com.Gallery.service.impl;

import com.Gallery.model.User;
import com.Gallery.repository.UserRepository;
import com.Gallery.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User createUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public User getUser(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

}
