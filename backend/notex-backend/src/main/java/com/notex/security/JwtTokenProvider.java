package com.notex.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * JwtTokenProvider - Creates and validates JWT tokens
 * 
 * JWT (JSON Web Token) is like a secure ID card:
 * - User logs in -> gets JWT token
 * - Every request includes this token
 * - Server verifies token to know who you are
 * 
 * JWT has 3 parts (separated by dots):
 * header.payload.signature
 * 
 * Example:
 * eyJhbGc...xyz.eyJ1c2Vy...abc.SflKx...123
 */
@Component
public class JwtTokenProvider {
    
    // Load from application.properties
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private long jwtExpiration;  // In milliseconds (default 24 hours)
    
    /**
     * Generate JWT token for a user
     * 
     * @param username - The username to put in the token
     * @return JWT token string
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        
        return Jwts.builder()
                .subject(username)              // Who the token is for
                .issuedAt(now)                 // When it was created
                .expiration(expiryDate)        // When it expires
                .signWith(getSigningKey())     // Sign with secret key
                .compact();                    // Build the token
    }
    
    /**
     * Extract username from JWT token
     * 
     * @param token - JWT token string
     * @return username
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())   // Verify signature
                .build()
                .parseSignedClaims(token)      // Parse the token
                .getPayload();                 // Get the data
        
        return claims.getSubject();            // Get username
    }
    
    /**
     * Validate JWT token
     * 
     * Checks:
     * - Is signature valid?
     * - Is token expired?
     * - Is token malformed?
     * 
     * @param token - JWT token string
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            
            return true;  // Token is valid!
            
        } catch (JwtException | IllegalArgumentException e) {
            // Token is invalid
            return false;
        }
    }
    
    /**
     * Get signing key from secret
     * This key is used to sign and verify tokens
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}