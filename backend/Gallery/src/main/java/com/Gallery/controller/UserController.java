package com.Gallery.controller;

import com.Gallery.dto.UserRegistrationDTO;
import com.Gallery.model.User;
import com.Gallery.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRegistrationDTO userDTO) {
        try {
            userDTO.validateUsername();
            userDTO.validateEmail();
            userDTO.validatePassword();
            User createdUser = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser); // 201 Created on success
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + ". Please check your details." + "\"}"); // 400 Bad Request for validation issues
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Username or email already in use. Please try again.\"}"); // 500 Internal Server Error for unknown issues
        }
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }

}
