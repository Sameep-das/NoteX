package com.notex.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JwtAuthenticationFilter - Runs on EVERY request
 * 
 * This filter:
 * 1. Looks for JWT token in request header
 * 2. Validates the token
 * 3. Sets authentication in Spring Security context
 * 
 * Think of it as a checkpoint - every request goes through here
 * 
 * OncePerRequestFilter ensures it runs exactly once per request
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    /**
     * This method runs on EVERY request to your API
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // Step 1: Get JWT token from request header
            String jwt = getJwtFromRequest(request);
            
            // Step 2: Validate token
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                
                // Step 3: Get username from token
                String username = jwtTokenProvider.getUsernameFromToken(jwt);
                
                // Step 4: Load user details from database
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // Step 5: Create authentication object
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                
                // Step 6: Set authentication in Spring Security context
                // Now Spring knows who the current user is!
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            
        } catch (Exception ex) {
            // If anything goes wrong, just continue (user won't be authenticated)
            logger.error("Could not set user authentication", ex);
        }
        
        // Continue with the request
        filterChain.doFilter(request, response);
    }
    
    /**
     * Extract JWT token from Authorization header
     * 
     * Header format: "Authorization: Bearer <token>"
     * We need to remove "Bearer " prefix
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // Remove "Bearer " prefix (7 characters)
            return bearerToken.substring(7);
        }
        
        return null;
    }
}