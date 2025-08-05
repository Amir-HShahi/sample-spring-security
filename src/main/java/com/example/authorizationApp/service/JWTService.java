/**
 * Service class for handling JSON Web Token (JWT) operations
 * This class provides functionality for generating, validating and extracting information from JWTs
 */
package com.example.authorizationApp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JWTService {

    /**
     * Secret key used for signing JWTs, injected from application properties
     */
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * Generates a JWT token for the given username
     * @param username The username to generate token for
     * @return JWT token string
     */
    public String generateToken(String username) {
        HashMap<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token valid for 10 hours
                .and()
                .signWith(getKey())
                .compact();
    }

    /**
     * Creates a signing key from the secret key
     * @return Key object used for signing tokens
     */
    private Key getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Extracts the username from a JWT token
     * @param token JWT token to extract username from
     * @return Username string
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Generic method to extract a claim from a JWT token
     * @param token JWT token to extract claim from
     * @param claimsResolver Function to resolve the desired claim
     * @return Claim value of type T
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from a JWT token
     * @param token JWT token to extract claims from
     * @return Claims object containing all claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Validates a JWT token against user details and expiration
     * @param token JWT token to validate
     * @param userDetails UserDetails object to validate against
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Checks if a JWT token is expired
     * @param token JWT token to check
     * @return true if token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from a JWT token
     * @param token JWT token to extract expiration from
     * @return Date object representing token expiration
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
