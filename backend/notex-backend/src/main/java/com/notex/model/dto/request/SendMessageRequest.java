package com.notex.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.notex.model.enums.MessageType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {
    
    @NotNull(message = "Group ID is required")
    private Long groupId;
    
    private MessageType messageType = MessageType.TEXT;
    
    private String content;
    
    private String fileUrl;
    
    private Long noteId;
}
