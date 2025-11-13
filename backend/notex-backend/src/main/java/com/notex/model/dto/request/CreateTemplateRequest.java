package com.notex.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTemplateRequest {
    
    @NotBlank(message = "Template name is required")
    private String name;
    
    private String description;
    
    @NotBlank(message = "Template data is required")
    private String templateData;
    
    private Boolean isPublic = true;
}