package com.notex.model.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.notex.model.enums.FriendshipStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipResponse {
    
    private Long id;
    private UserResponse user;
    private UserResponse friend;
    private FriendshipStatus status;
    private LocalDateTime createdAt;
}