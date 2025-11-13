// package com.notex.service;

// import org.springframework.stereotype.Service;
// import org.springframework.beans.factory.annotation.Autowired;
// import lombok.Getter;
// import lombok.Setter;

// import java.util.Optional;
// import java.util.List;

// import com.notex.repository.UserRepository;
// import com.notex.model.entity.User;

// @Setter
// @Getter
// @Service
// public class UserService {

//     @Autowired
//     private UserRepository userRepository;

//     public Optional<User> findById(Long id) {
//         return userRepository.findById(id);
//     }

//     public Optional<User> findByUsername(String username) {
//         return userRepository.findByUsername(username);
//     }

//     public User updateUser(Long id, User user) {
//         return userRepository.findById(id).map(existing -> {
//             existing.setDisplayName(user.getDisplayName());
//             existing.setEmail(user.getEmail());
//             // copy other allowed fields
//             return userRepository.save(existing);
//         }).orElseThrow(() -> new RuntimeException("User not found"));
//     }

//     public List<User> searchUsers(String query, int page, int size) {
//         // simple search by username or displayName - implement repository method if needed
//         return userRepository.findByUsernameContainingIgnoreCaseOrDisplayNameContainingIgnoreCase(query, query);
//     }

//     public void changePassword(Long userId, String oldPassword, String newPassword) {
//         userRepository.findById(userId).ifPresent(user -> {
//             // password change flow should verify oldPassword - omitted for brevity
//             user.setPassword(newPassword);
//             userRepository.save(user);
//         });
//     }
// }

//New Code -

package com.notex.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import com.notex.model.entity.User;
import com.notex.model.dto.request.UpdateProfileRequest;
import com.notex.model.dto.response.UserResponse;
import com.notex.repository.UserRepository;
import com.notex.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return modelMapper.map(user, UserResponse.class);
    }
    
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return modelMapper.map(user, UserResponse.class);
    }
    
    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getIsPublic() != null) {
            user.setIsPublic(request.getIsPublic());
        }
        
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserResponse.class);
    }
    
    @Transactional
    public void updateOnlineStatus(String username, Boolean isOnline) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        userRepository.updateOnlineStatus(user.getId(), isOnline, LocalDateTime.now());
    }
    
    public List<UserResponse> getOnlineUsers() {
        return userRepository.findByIsOnlineTrue()
            .stream()
            .map(user -> modelMapper.map(user, UserResponse.class))
            .collect(Collectors.toList());
    }
    
    @Transactional
    public UserResponse updateProfilePicture(Long userId, String pictureUrl) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setProfilePictureUrl(pictureUrl);
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserResponse.class);
    }
}

