package com.Gallery.service;

import com.Gallery.model.User;

import java.util.UUID;

public interface UserService {
    User createUser(User user);
    User getUser(UUID id);
}
