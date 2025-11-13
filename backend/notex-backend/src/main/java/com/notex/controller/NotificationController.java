package com.notex.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.notex.service.NotificationService;
import com.notex.model.dto.response.NotificationResponse;
import com.notex.model.dto.response.PageResponse;
import com.notex.model.dto.response.ApiResponse;
import com.notex.security.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        PageResponse<NotificationResponse> notifications = 
            notificationService.getUserNotifications(currentUser.getId(), page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "Notifications retrieved", notifications));
    }
    
    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUnreadNotifications(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<NotificationResponse> notifications = 
            notificationService.getUnreadNotifications(currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Unread notifications retrieved", notifications));
    }
    
    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        Long count = notificationService.getUnreadCount(currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Unread count retrieved", count));
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        notificationService.markAsRead(id, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Notification marked as read"));
    }
    
    @PutMapping("/mark-all-read")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        notificationService.markAllAsRead(currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "All notifications marked as read"));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        notificationService.deleteNotification(id, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Notification deleted"));
    }
}