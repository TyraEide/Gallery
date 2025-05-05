package com.Gallery.controller;

import com.Gallery.model.CanvasToken;
import com.Gallery.model.User;
import com.Gallery.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tokens")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createToken(@RequestBody CanvasToken canvasToken, @AuthenticationPrincipal User auth) {
        tokenService.create(canvasToken, auth);
    }

    @GetMapping("/user")
    public void getAllTokensForUser(@RequestBody User user, @AuthenticationPrincipal User auth) {
        tokenService.getAllTokensForUser(user, auth);
    }
}
