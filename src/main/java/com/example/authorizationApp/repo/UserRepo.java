package com.example.authorizationApp.repo;

import com.example.authorizationApp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing User entities
 * Extends JpaRepository to inherit basic CRUD operations and pagination functionality
 */
@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {
    
    /**
     * Finds a user by their username
     * @param username The username to search for
     * @return The Users entity if found, null otherwise
     */
    Users findByUsername(String username);
}
