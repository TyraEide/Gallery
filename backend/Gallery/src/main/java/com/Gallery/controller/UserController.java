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
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO userDTO) {
        try {
            User createdUser = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser); // 201 Created on success
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}"); // 400 Bad Request for validation issues
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"An unexpected error occurred.\"}"); // 500 Internal Server Error for unknown issues
        }
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}/setUibToken")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody User setUibToken(@RequestBody String token, @PathVariable UUID id, @AuthenticationPrincipal User auth) {
        return userService.setUibToken(token, id, auth);
    }

    @PutMapping("/{id}/setHvlToken")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody User setHvlToken(@RequestBody String token, @PathVariable UUID id, @AuthenticationPrincipal User auth) {
        return userService.setHvlToken(token, id, auth);
    }

}
