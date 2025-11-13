package com.notex.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.notex.model.enums.NoteType;
import com.notex.model.enums.Visibility;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateNoteRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String content;
    
    @NotNull(message = "Note type is required")
    private NoteType noteType;
    
    private Visibility visibility = Visibility.PRIVATE;
    
    private Long groupId;
    
    private Set<Long> tagIds;
}