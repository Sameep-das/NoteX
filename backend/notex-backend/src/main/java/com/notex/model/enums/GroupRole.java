package com.notex.model.enums;

public enum GroupRole {
    ADMIN,      // Full control - can delete group, remove members
    MODERATOR,  // Can manage content, add/remove members
    MEMBER      // Regular member - can view and post
}