package com.Gallery.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

/**
 * Source: <a href="https://www.youtube.com/watch?v=us0VjFiHogo">...</a>
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "This will be the dashboard, or redirect to login if not authenticated.";
    }

    @GetMapping("/login")
    public String login() {
        return "This will become the login page.";
    }

    @GetMapping("/profile")
    public String profile() {
        return "Page for editing profile.";
    }


}
