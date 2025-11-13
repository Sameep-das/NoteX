package com.notex.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private String profilePictureUrl;
    
    public AuthResponse(String token, Long userId, String username, String email, 
                       String fullName, String profilePictureUrl) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.profilePictureUrl = profilePictureUrl;
    }
}
