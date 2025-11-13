package com.notex.model.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateResponse {
    
    private Long id;
    private String name;
    private String description;
    private String templateData;
    private String thumbnailUrl;
    private UserResponse createdBy;
    private Boolean isPublic;
    private Integer usageCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}