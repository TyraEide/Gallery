package com.Gallery.service;

import com.Gallery.model.User;

import java.util.UUID;

public interface UserService {
    User createUser(User user);
    User getUser(UUID id);
    User setUibToken(String token, UUID userId, User auth);
    User setHvlToken(String token, UUID userId, User auth);
}
