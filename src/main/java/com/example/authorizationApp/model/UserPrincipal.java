package com.example.authorizationApp.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * UserPrincipal class implements UserDetails interface from Spring Security
 * This class represents the currently authenticated user and provides core user information
 */
public class UserPrincipal implements UserDetails {
    
    /**
     * The user entity containing the user's details
     */
    private final Users user;

    /**
     * Constructor to create a new UserPrincipal
     * @param user The Users entity to wrap
     */
    public UserPrincipal(Users user) {
        this.user = user;
    }

    /**
     * Returns the authorities granted to the user
     * Currently only assigns "USER" role to all users
     * @return A collection containing the user's authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    /**
     * Returns the password used to authenticate the user
     * @return The user's password
     */
    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    /**
     * Returns the username used to authenticate the user
     * @return The user's username
     */
    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    /**
     * Indicates whether the user's account has expired
     * @return true if the user's account is valid (not expired)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked
     * @return true if the user is not locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired
     * @return true if the user's credentials are valid (not expired)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled
     * @return true if the user is enabled
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
