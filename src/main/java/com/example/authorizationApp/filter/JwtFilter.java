/**
 * JWT Authentication Filter
 * This filter intercepts all incoming HTTP requests and validates JWT tokens
 * for authentication purposes. It extends OncePerRequestFilter to ensure
 * the filter is only executed once per request.
 */
package com.example.authorizationApp.filter;

import com.example.authorizationApp.service.CustomizedUserDetailsService;
import com.example.authorizationApp.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Component class that handles JWT token validation and authentication
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    /**
     * Service for JWT operations like token validation and username extraction
     */
    @Autowired
    private JWTService jwtService;

    /**
     * Service for loading user details from the database
     */
    @Autowired
    private CustomizedUserDetailsService userDetailsService;

    /**
     * Main filter method that processes each HTTP request
     * @param request The HTTP request
     * @param response The HTTP response
     * @param filterChain The filter chain for additional filters
     * @throws ServletException If a servlet error occurs
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Extract the Authorization header from the request
        String authHeader = request.getHeader("Authorization");

        // Check if Authorization header exists and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extract the JWT token (remove "Bearer " prefix)
            String token = authHeader.substring(7);
            // Extract username from token
            String username = jwtService.extractUsername(token);

            // Proceed if username exists and no authentication is already present
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Load user details from database
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validate the token against user details
                if (jwtService.validateToken(token, userDetails)) {
                    // Create authentication token with user details and authorities
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    // Set additional authentication details from the request
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Set the authentication in the SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
