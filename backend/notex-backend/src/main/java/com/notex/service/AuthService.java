// package com.notex.service;

// // Spring annotations
// import org.springframework.stereotype.Service;
// import org.springframework.beans.factory.annotation.Autowired;

// // Spring Security
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.crypto.password.PasswordEncoder;

// // ModelMapper for entity-DTO conversion
// import org.modelmapper.ModelMapper;

// // Your project classes
// import com.notex.model.entity.User;
// import com.notex.model.dto.request.LoginRequest;
// import com.notex.model.dto.request.RegisterRequest;
// import com.notex.model.dto.response.AuthResponse;
// import com.notex.model.dto.response.UserResponse;
// import com.notex.repository.UserRepository;
// import com.notex.security.JwtTokenProvider;
// import com.notex.exception.BadRequestException;
// import com.notex.exception.ResourceNotFoundException;

// /**
//  * AuthService - Handles authentication business logic
//  * 
//  * @Service tells Spring this is a service component
//  * Services contain business logic (the "brain" of your app)
//  * 
//  * Flow:
//  * Controller receives request -> calls Service -> Service uses Repository -> returns to Controller
//  */
// @Service
// public class AuthService {
    
//     /**
//      * Dependency Injection - Spring provides these automatically
//      */
//     @Autowired
//     private UserRepository userRepository;
    
//     @Autowired
//     private PasswordEncoder passwordEncoder;  // BCrypt password encryption
    
//     @Autowired
//     private JwtTokenProvider jwtTokenProvider;  // Creates JWT tokens
    
//     @Autowired
//     private AuthenticationManager authenticationManager;  // Handles authentication
    
//     @Autowired
//     private ModelMapper modelMapper;  // Converts Entity <-> DTO
    
//     /**
//      * Register a new user
//      * 
//      * Steps:
//      * 1. Check if username/email already exists
//      * 2. Create new User entity
//      * 3. Hash the password (never store plain text!)
//      * 4. Save to database
//      * 5. Generate JWT token
//      * 6. Return token and user info
//      */
//     public AuthResponse register(RegisterRequest request) {
        
//         // Check if username already taken
//         if (userRepository.existsByUsername(request.getUsername())) {
//             throw new BadRequestException("Username is already taken!");
//         }
        
//         // Check if email already registered
//         if (userRepository.existsByEmail(request.getEmail())) {
//             throw new BadRequestException("Email is already registered!");
//         }
        
//         // Create new User entity
//         User user = new User();
//         user.setUsername(request.getUsername());
//         user.setEmail(request.getEmail());
        
//         // Hash the password using BCrypt
//         // Never store passwords in plain text!
//         user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        
//         user.setFullName(request.getFullName());
//         user.setIsPublic(request.getIsPublic() != null ? request.getIsPublic() : true);
//         user.setIsOnline(true);  // User is online when they register
        
//         // Save to database
//         User savedUser = userRepository.save(user);
        
//         // Generate JWT token
//         String token = jwtTokenProvider.generateToken(savedUser.getUsername());
        
//         // Create and return response
//         return new AuthResponse(
//             token,
//             savedUser.getId(),
//             savedUser.getUsername(),
//             savedUser.getEmail(),
//             savedUser.getFullName(),
//             savedUser.getProfilePictureUrl()
//         );
//     }
    
//     /**
//      * Login existing user
//      * 
//      * Steps:
//      * 1. Find user by username or email
//      * 2. Verify password
//      * 3. Generate JWT token
//      * 4. Update online status
//      * 5. Return token and user info
//      */
//     public AuthResponse login(LoginRequest request) {
        
//         // Find user by username or email
//         User user = userRepository.findByUsernameOrEmail(
//             request.getUsernameOrEmail(),
//             request.getUsernameOrEmail()
//         ).orElseThrow(() -> 
//             new BadRequestException("Invalid username/email or password")
//         );
        
//         // Authenticate using Spring Security
//         // This checks if password is correct
//         try {
//             Authentication authentication = authenticationManager.authenticate(
//                 new UsernamePasswordAuthenticationToken(
//                     user.getUsername(),
//                     request.getPassword()
//                 )
//             );
            
//             // Set authentication in Spring Security context
//             SecurityContextHolder.getContext().setAuthentication(authentication);
            
//         } catch (Exception e) {
//             throw new BadRequestException("Invalid username/email or password");
//         }
        
//         // Update user's online status
//         user.setIsOnline(true);
//         userRepository.save(user);
        
//         // Generate JWT token
//         String token = jwtTokenProvider.generateToken(user.getUsername());
        
//         // Return response
//         return new AuthResponse(
//             token,
//             user.getId(),
//             user.getUsername(),
//             user.getEmail(),
//             user.getFullName(),
//             user.getProfilePictureUrl()
//         );
//     }
    
//     /**
//      * Get current user info (for /auth/me endpoint)
//      * 
//      * @param userId - ID from JWT token
//      */
//     public UserResponse getCurrentUser(Long userId) {
        
//         // Find user in database
//         User user = userRepository.findById(userId)
//             .orElseThrow(() -> 
//                 new ResourceNotFoundException("User not found")
//             );
        
//         // Convert User entity to UserResponse DTO
//         // ModelMapper automatically maps matching fields
//         return modelMapper.map(user, UserResponse.class);
//     }
    
//     /**
//      * Helper method to get User entity by username
//      * Used by other services
//      */
//     public User getUserByUsername(String username) {
//         return userRepository.findByUsername(username)
//             .orElseThrow(() -> 
//                 new ResourceNotFoundException("User not found with username: " + username)
//             );
//     }
    
//     /**
//      * Helper method to get User entity by ID
//      * Used by other services
//      */
//     public User getUserById(Long userId) {
//         return userRepository.findById(userId)
//             .orElseThrow(() -> 
//                 new ResourceNotFoundException("User not found with id: " + userId)
//             );
//     }
// }

//New Code -

package com.notex.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;

import com.notex.model.entity.User;
import com.notex.model.dto.request.LoginRequest;
import com.notex.model.dto.request.RegisterRequest;
import com.notex.model.dto.response.AuthResponse;
import com.notex.model.dto.response.UserResponse;
import com.notex.repository.UserRepository;
import com.notex.security.JwtTokenProvider;
import com.notex.exception.BadRequestException;
import com.notex.exception.ResourceNotFoundException;

import java.time.LocalDateTime;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private ModelMapper modelMapper;
    
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered!");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setIsPublic(request.getIsPublic() != null ? request.getIsPublic() : true);
        user.setIsOnline(true);
        
        User savedUser = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(savedUser.getUsername());
        
        return new AuthResponse(
            token,
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getEmail(),
            savedUser.getFullName(),
            savedUser.getProfilePictureUrl()
        );
    }
    
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsernameOrEmail(
            request.getUsernameOrEmail(),
            request.getUsernameOrEmail()
        ).orElseThrow(() -> 
            new BadRequestException("Invalid username/email or password")
        );
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    request.getPassword()
                )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new BadRequestException("Invalid username/email or password");
        }
        
        user.setIsOnline(true);
        user.setLastSeen(LocalDateTime.now());
        userRepository.save(user);
        
        String token = jwtTokenProvider.generateToken(user.getUsername());
        
        return new AuthResponse(
            token,
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFullName(),
            user.getProfilePictureUrl()
        );
    }
    
    public UserResponse getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return modelMapper.map(user, UserResponse.class);
    }
    
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }
    
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }
}
