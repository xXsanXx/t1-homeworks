package com.example.authorization.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestController {

    @RequestMapping("/all")
    public String getAll() {
        return "public API";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('GUEST') or hasRole('PREMIUM_USER') or hasRole('ADMIN')")
    public String userAccess() {
        return "user API";
    }

    @GetMapping("/prem")
    @PreAuthorize("hasRole('PREMIUM_USER') or hasRole('ADMIN')")
    public String moderatorAccess() {
        return "PREMIUM_USER API";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "admin API";
    }


}
