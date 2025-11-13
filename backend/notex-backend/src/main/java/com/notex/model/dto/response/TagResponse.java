package com.notex.model.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagResponse {
    
    private Long id;
    private String name;
    private String category;
    private LocalDateTime createdAt;
}