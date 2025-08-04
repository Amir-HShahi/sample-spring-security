package com.example.authorizationApp.controller;

import com.example.authorizationApp.model.Users;
import com.example.authorizationApp.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {
    @Autowired
    private UsersService usersService;

    @PostMapping("/register")
    public Users registerUsers(@RequestBody Users user) {
        return usersService.registerUser(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody Users user) {
        return usersService.verify(user);
    }
}
