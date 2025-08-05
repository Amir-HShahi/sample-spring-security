/**
 * Service class for handling user-related operations including registration and authentication.
 * This service provides methods for user registration with password encryption and user verification.
 */
package com.example.authorizationApp.service;

import com.example.authorizationApp.model.Users;
import com.example.authorizationApp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    /**
     * BCrypt password encoder with strength 12 for secure password hashing
     */
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    /**
     * Repository for user data persistence
     */
    @Autowired
    private UserRepo userRepo;

    /**
     * Authentication manager for handling user authentication
     */
    @Autowired
    private AuthenticationManager authManager;

    /**
     * Service for JWT token generation and management
     */
    @Autowired
    private JWTService jwtService;

    /**
     * Registers a new user in the system
     * @param user The user object containing registration details
     * @return The saved user object with encrypted password
     */
    public Users registerUser(Users user) {
        final String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepo.save(user);
    }

    /**
     * Verifies user credentials and generates JWT token upon successful authentication
     * @param user The user object containing login credentials
     * @return JWT token if authentication is successful, "fail" otherwise
     */
    public String verify(Users user) {
        Authentication auth = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        return auth.isAuthenticated() ? jwtService.generateToken(user.getUsername()) : "Fail";
    }
}
