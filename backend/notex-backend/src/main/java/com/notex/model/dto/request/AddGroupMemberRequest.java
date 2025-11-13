package com.notex.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.notex.model.enums.GroupRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddGroupMemberRequest {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    private GroupRole role = GroupRole.MEMBER;
}
