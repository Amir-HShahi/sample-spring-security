/**
 * Security configuration class for the authorization application.
 * This class configures Spring Security settings including authentication, authorization,
 * session management, and JWT filter integration.
 */
package com.example.authorizationApp.config;

import com.example.authorizationApp.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main security configuration class
 * Enables web security and provides configuration beans
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Service to load user-specific data
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Filter for handling JWT based authentication
     */
    @Autowired
    private JwtFilter jwtFilter;

    /**
     * Configures the security filter chain
     * @param http HttpSecurity object to configure
     * @return Configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF protection
                .authorizeHttpRequests(request -> request
                        .requestMatchers("register", "login")  // Public endpoints
                        .permitAll()
                        .anyRequest().authenticated())  // All other requests require authentication
                .oauth2Login(Customizer.withDefaults())  // Enable OAuth2 login
                .httpBasic(Customizer.withDefaults())  // Enable HTTP Basic authentication
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Use stateless sessions
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)  // Add JWT filter
                .build();
    }

    /**
     * Configures the authentication provider
     * @return Configured DaoAuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));  // Use BCrypt with strength 12
        return authenticationProvider;
    }

    /**
     * Creates the authentication manager
     * @param authConfig Authentication configuration
     * @return AuthenticationManager instance
     * @throws Exception if manager creation fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
