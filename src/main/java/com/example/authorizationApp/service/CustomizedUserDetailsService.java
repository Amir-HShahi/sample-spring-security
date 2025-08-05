/**
 * Custom implementation of Spring Security's UserDetailsService.
 * This service is responsible for loading user-specific data during authentication.
 */
package com.example.authorizationApp.service;

import com.example.authorizationApp.model.Users;
import com.example.authorizationApp.model.UserPrincipal;
import com.example.authorizationApp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomizedUserDetailsService implements UserDetailsService {
    /**
     * Repository for accessing user data
     */
    @Autowired
    private UserRepo userRepo;

    /**
     * Loads a user by their username during authentication
     * 
     * @param username The username to search for
     * @return UserDetails object containing the user's security information
     * @throws UsernameNotFoundException if user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUsername(username);
        if (user == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException(username);
        }
        return new UserPrincipal(user);
    }
}
