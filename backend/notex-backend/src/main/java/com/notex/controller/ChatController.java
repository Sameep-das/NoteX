package com.notex.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.notex.service.ChatService;
import com.notex.model.dto.request.SendMessageRequest;
import com.notex.model.dto.response.ChatMessageResponse;
import com.notex.model.dto.response.PageResponse;
import com.notex.model.dto.response.ApiResponse;
import com.notex.security.UserPrincipal;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {
    
    @Autowired
    private ChatService chatService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @MessageMapping("/chat.sendMessage/{groupId}")
    public void sendMessage(
            @DestinationVariable Long groupId,
            @Payload SendMessageRequest request,
            Principal principal) {
        
        ChatMessageResponse message = chatService.sendMessage(groupId, request, principal.getName());
        messagingTemplate.convertAndSend("/topic/group/" + groupId, message);
    }
    
    @MessageMapping("/chat.addUser/{groupId}")
    public void addUser(
            @DestinationVariable Long groupId,
            @Payload String username,
            Principal principal) {
        
        ChatMessageResponse message = new ChatMessageResponse();
        message.setContent(username + " joined the chat");
        messagingTemplate.convertAndSend("/topic/group/" + groupId, message);
    }
}

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
class ChatRestController {
    
    @Autowired
    private ChatService chatService;
    
    @GetMapping("/groups/{groupId}/messages")
    public ResponseEntity<ApiResponse<PageResponse<ChatMessageResponse>>> getGroupMessages(
            @PathVariable Long groupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        PageResponse<ChatMessageResponse> messages = chatService.getGroupMessages(groupId, page, size, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Messages retrieved", messages));
    }
    
    @GetMapping("/groups/{groupId}/recent")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getRecentMessages(
            @PathVariable Long groupId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<ChatMessageResponse> messages = chatService.getRecentMessages(groupId, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Recent messages retrieved", messages));
    }
    
    @PutMapping("/messages/{messageId}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable Long messageId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        chatService.markAsRead(messageId, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Message marked as read"));
    }
    
    @GetMapping("/groups/{groupId}/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
            @PathVariable Long groupId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        Long count = chatService.getUnreadCount(groupId, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Unread count retrieved", count));
    }
}