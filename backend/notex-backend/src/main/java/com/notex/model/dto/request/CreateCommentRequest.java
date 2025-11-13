package com.notex.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {
    
    @NotNull(message = "Note ID is required")
    private Long noteId;
    
    @NotBlank(message = "Comment content is required")
    private String content;
    
    private Long parentCommentId;
}