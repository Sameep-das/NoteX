package com.notex.model.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.notex.model.enums.GroupRole;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberResponse {
    
    private Long id;
    private UserResponse user;
    private GroupRole role;
    private LocalDateTime joinedAt;
}
