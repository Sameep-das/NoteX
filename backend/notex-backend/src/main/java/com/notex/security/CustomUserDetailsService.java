package com.notex.security;

import com.notex.model.entity.User;
import com.notex.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CustomUserDetailsService - Loads user data for Spring Security
 * 
 * Spring Security needs to know:
 * - How to find a user (by username)
 * - What their password is
 * - What permissions they have
 * 
 * This service provides that information
 * 
 * UserDetailsService is a Spring Security interface
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Load user by username
     * 
     * This is called by Spring Security during login
     * and when validating JWT tokens
     * 
     * @param username - The username to search for
     * @return UserDetails object with user information
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) 
            throws UsernameNotFoundException {
        
        // Find user in database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> 
                    new UsernameNotFoundException("User not found with username: " + username)
                );
        
        // Convert User entity to UserPrincipal (which implements UserDetails)
        return UserPrincipal.create(user);
    }
    
    /**
     * Load user by ID
     * 
     * This is used when we have a user ID from JWT token
     * and need to load their full details
     * 
     * @param id - The user ID
     * @return UserDetails object
     */
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> 
                    new UsernameNotFoundException("User not found with id: " + id)
                );
        
        return UserPrincipal.create(user);
    }
}