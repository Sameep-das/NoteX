package com.notex.config;

import com.notex.security.JwtAuthenticationFilter;
import com.notex.security.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * SecurityConfig - Main security configuration
 * 
 * This is the MOST IMPORTANT configuration file!
 * It controls:
 * - Which endpoints require authentication
 * - How passwords are encrypted
 * - CORS (Cross-Origin Resource Sharing)
 * - JWT token validation
 * 
 * @Configuration tells Spring this is a configuration class
 * @EnableWebSecurity enables Spring Security
 * @EnableMethodSecurity allows @PreAuthorize annotations
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    /**
     * SecurityFilterChain - Configures HTTP security
     * This is like a bouncer at a club - decides who gets in
     * 
     * @Bean means Spring will manage this and make it available everywhere
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (Cross-Site Request Forgery)
            // We don't need it because we're using JWT tokens
            .csrf(csrf -> csrf.disable())
            
            // Enable CORS (allows React frontend to call API)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Configure which endpoints require authentication
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (no authentication needed)
                .requestMatchers(
                    "/auth/**",           // Login, register
                    "/ws/**",             // WebSocket connection
                    "/error"              // Error page
                ).permitAll()
                
                // All other endpoints require authentication
                .anyRequest().authenticated()
            )
            
            // Stateless session management (no server-side sessions)
            // Each request must include JWT token
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Add our custom JWT filter before Spring's default filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            
            // Set authentication provider
            .authenticationProvider(authenticationProvider());
        
        return http.build();
    }
    
    /**
     * PasswordEncoder - Encrypts passwords
     * BCrypt is a strong hashing algorithm
     * 
     * Why encrypt?
     * - Never store plain text passwords!
     * - If database is hacked, passwords are safe
     * - BCrypt adds "salt" so same password = different hash each time
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * AuthenticationManager - Handles authentication process
     * This is used when user logs in to verify username/password
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    /**
     * AuthenticationProvider - Tells Spring how to authenticate users
     * Uses our CustomUserDetailsService to load user from database
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    
    /**
     * CORS Configuration - Allows frontend to call backend
     * 
     * CORS is needed when:
     * - Frontend: http://localhost:3000
     * - Backend: http://localhost:8080
     * (Different ports = different origins)
     * 
     * Without CORS, browser blocks the request!
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow requests from these origins (your React app)
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",    // React default
            "http://localhost:5173"     // Vite default
        ));
        
        // Allow these HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));
        
        // Allow all headers
        configuration.setAllowedHeaders(List.of("*"));
        
        // Allow credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // Apply CORS to all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
