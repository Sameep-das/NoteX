package com.notex.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDto {
    
    @NotNull(message = "Friend user ID is required")
    private Long friendId;
}