package com.notex.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import com.notex.service.UserService;
import com.notex.service.FileStorageService;
import com.notex.model.dto.request.UpdateProfileRequest;
import com.notex.model.dto.response.UserResponse;
import com.notex.model.dto.response.ApiResponse;
import com.notex.security.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User retrieved", user));
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByUsername(@PathVariable String username) {
        UserResponse user = userService.getUserByUsername(username);
        return ResponseEntity.ok(new ApiResponse<>(true, "User retrieved", user));
    }
    
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        UserResponse user = userService.updateProfile(currentUser.getId(), request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated", user));
    }
    
    @PostMapping("/profile-picture")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfilePicture(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        String fileUrl = fileStorageService.storeFile(file);
        UserResponse user = userService.updateProfilePicture(currentUser.getId(), fileUrl);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile picture updated", user));
    }
    
    @GetMapping("/online")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getOnlineUsers() {
        List<UserResponse> users = userService.getOnlineUsers();
        return ResponseEntity.ok(new ApiResponse<>(true, "Online users retrieved", users));
    }
}