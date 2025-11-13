package com.notex.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import jakarta.validation.Valid;

import com.notex.service.FriendshipService;
import com.notex.model.dto.request.FriendRequestDto;
import com.notex.model.dto.response.FriendshipResponse;
import com.notex.model.dto.response.UserResponse;
import com.notex.model.dto.response.ApiResponse;
import com.notex.security.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/friends")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class FriendshipController {
    
    @Autowired
    private FriendshipService friendshipService;
    
    @PostMapping("/request")
    public ResponseEntity<ApiResponse<FriendshipResponse>> sendFriendRequest(
            @Valid @RequestBody FriendRequestDto request,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        FriendshipResponse friendship = friendshipService.sendFriendRequest(currentUser.getId(), request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Friend request sent", friendship));
    }
    
    @PutMapping("/{id}/accept")
    public ResponseEntity<ApiResponse<FriendshipResponse>> acceptFriendRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        FriendshipResponse friendship = friendshipService.acceptFriendRequest(id, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Friend request accepted", friendship));
    }
    
    @DeleteMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectFriendRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        friendshipService.rejectFriendRequest(id, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Friend request rejected"));
    }
    
    @DeleteMapping("/{friendId}")
    public ResponseEntity<ApiResponse<Void>> removeFriend(
            @PathVariable Long friendId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        friendshipService.removeFriend(friendId, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Friend removed"));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getFriends(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<UserResponse> friends = friendshipService.getFriends(currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Friends retrieved", friends));
    }
    
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<FriendshipResponse>>> getPendingRequests(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<FriendshipResponse> requests = friendshipService.getPendingRequests(currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Pending requests retrieved", requests));
    }
    
    @GetMapping("/online")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getOnlineFriends(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<UserResponse> friends = friendshipService.getOnlineFriends(currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Online friends retrieved", friends));
    }
}