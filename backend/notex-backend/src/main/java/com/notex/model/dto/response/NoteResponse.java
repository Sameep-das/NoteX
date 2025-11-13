package com.notex.model.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.notex.model.enums.NoteType;
import com.notex.model.enums.Visibility;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {
    
    private Long id;
    private String title;
    private String content;
    private NoteType noteType;
    private Visibility visibility;
    private String fileUrl;
    private String thumbnailUrl;
    private Integer viewCount;
    private Integer likeCount;
    private UserResponse createdBy;
    private Long groupId;
    private String groupName;
    private Set<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
