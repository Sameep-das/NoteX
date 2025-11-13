package com.notex.model.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.notex.model.enums.MessageType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
    
    private Long id;
    private Long groupId;
    private UserResponse sender;
    private MessageType messageType;
    private String content;
    private String fileUrl;
    private Long noteId;
    private Boolean isRead;
    private LocalDateTime createdAt;
}