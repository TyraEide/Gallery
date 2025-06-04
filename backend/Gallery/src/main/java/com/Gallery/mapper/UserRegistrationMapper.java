package com.Gallery.mapper;

import com.Gallery.dto.UserRegistrationDTO;
import com.Gallery.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationMapper {
    public UserRegistrationDTO toDTO(User user) {
        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        return dto;
    }

    public User toEntity(UserRegistrationDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

}
