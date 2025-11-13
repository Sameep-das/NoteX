package com.notex.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTagRequest {
    
    @NotBlank(message = "Tag name is required")
    private String name;
    
    private String category;
}