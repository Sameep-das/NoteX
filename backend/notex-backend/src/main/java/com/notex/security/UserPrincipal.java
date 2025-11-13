package com.notex.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.notex.model.entity.User;

/**
 * UserPrincipal - Represents the currently authenticated user
 * 
 * This class implements UserDetails, which is required by Spring Security
 * It contains all the information Spring Security needs about a user
 * 
 * Think of it as a "Security ID Card" for the user
 */
public class UserPrincipal implements UserDetails {
    
    private Long id;
    private String username;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    
    /**
     * Constructor
     */
    public UserPrincipal(
            Long id,
            String username,
            String email,
            String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }
    
    /**
     * Factory method to create UserPrincipal from User entity
     * 
     * @param user - User entity from database
     * @return UserPrincipal object
     */
    public static UserPrincipal create(User user) {
        // For now, we're giving all users the "USER" role
        // You can add more complex roles later (ADMIN, MODERATOR, etc.)
        Collection<GrantedAuthority> authorities = Collections.singleton(
            new SimpleGrantedAuthority("ROLE_USER")
        );
        
        return new UserPrincipal(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPasswordHash(),
            authorities
        );
    }
    
    // ==================== UserDetails Interface Methods ====================
    // Spring Security requires these methods
    
    /**
     * Get user's authorities (roles/permissions)
     * Example: ROLE_USER, ROLE_ADMIN
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    /**
     * Get user's password
     * Used during authentication to compare with entered password
     */
    @Override
    public String getPassword() {
        return password;
    }
    
    /**
     * Get username
     */
    @Override
    public String getUsername() {
        return username;
    }
    
    /**
     * Is account not expired?
     * We always return true (accounts don't expire in our app)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    /**
     * Is account not locked?
     * We always return true (we don't lock accounts)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    /**
     * Are credentials not expired?
     * We always return true (passwords don't expire)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    /**
     * Is account enabled?
     * We always return true (all accounts are enabled)
     * You could add a "disabled" flag to User entity if needed
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    // ==================== Custom Getters ====================
    
    /**
     * Get user ID
     * This is NOT part of UserDetails interface, but we add it
     * because we need it in controllers
     */
    public Long getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
    // ==================== equals() and hashCode() ====================
    // These are important for Spring Security to work correctly
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}