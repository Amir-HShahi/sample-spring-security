/**
 * Controller class for handling user-related HTTP requests.
 * This class manages user registration and login endpoints.
 */
package com.example.authorizationApp.controller;

import com.example.authorizationApp.model.Users;
import com.example.authorizationApp.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing user operations.
 * Handles HTTP requests for user registration and authentication.
 */
@RestController
public class UsersController {
    /**
     * Service layer dependency for user operations
     */
    @Autowired
    private UsersService usersService;

    /**
     * Handles user registration requests
     * @param user The user object containing registration details
     * @return The registered user object
     */
    @PostMapping("/register")
    public Users registerUsers(@RequestBody Users user) {
        return usersService.registerUser(user);
    }

    /**
     * Handles user login requests
     * @param user The user object containing login credentials
     * @return A string indicating the login result/token
     */
    @PostMapping("/login")
    public String loginUser(@RequestBody Users user) {
        return usersService.verify(user);
    }
}
