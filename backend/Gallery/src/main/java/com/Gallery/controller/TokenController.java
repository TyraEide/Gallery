package com.Gallery.controller;

import com.Gallery.model.CanvasToken;
import com.Gallery.model.User;
import com.Gallery.service.TokenService;
import com.Gallery.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/tokens")
public class TokenController {

    private final TokenService tokenService;
    private final UserService userService;

    public TokenController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CanvasToken createToken(@RequestBody CanvasToken canvasToken) {
        return tokenService.create(canvasToken);
    }

    @GetMapping("/user/{id}")
    public List<CanvasToken> getAllTokensForUser(@PathVariable UUID id) {
        User user = userService.getUser(id);
        return tokenService.getAllTokensForUser(user);
    }
}
