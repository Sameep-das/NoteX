package com.notex.model.enums;

public enum MessageType {
    TEXT,        // Regular text message
    IMAGE,       // Image attachment
    FILE,        // File attachment
    NOTE_SHARE,  // Shared note
    JOIN,        // User joined chat (system message)
    LEAVE        // User left chat (system message)
}
