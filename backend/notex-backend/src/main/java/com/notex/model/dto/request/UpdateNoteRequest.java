package com.notex.model.dto.request;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.notex.model.enums.Visibility;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNoteRequest {
    
    private String title;
    private String content;
    private Visibility visibility;
    private Long groupId;
    private Set<Long> tagIds;
}