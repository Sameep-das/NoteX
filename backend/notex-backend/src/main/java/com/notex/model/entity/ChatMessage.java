// package com.notex.model.entity;

// import jakarta.persistence.Entity;
// import jakarta.persistence.Table;
// import jakarta.persistence.Id;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.Enumerated;
// import jakarta.persistence.EnumType;
// import jakarta.persistence.Column;

// import java.time.LocalDateTime;
// import lombok.Data;

// import com.notex.model.enums.MessageType;

// @Entity
// @Table(name = "chat_messages")
// @Data
// public class ChatMessage {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;
    
//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "group_id", nullable = false)
//     private Group group;
    
//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "sender_id", nullable = false)
//     private User sender;
    
//     @Enumerated(EnumType.STRING)
//     private MessageType messageType;
    
//     @Column(columnDefinition = "TEXT")
//     private String content;
    
//     private String fileUrl;
    
//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "note_id")
//     private Note note;
    
//     private Boolean isRead = false;
    
//     private LocalDateTime createdAt = LocalDateTime.now();
// }

//New code -
package com.notex.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.notex.model.enums.MessageType;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType messageType = MessageType.TEXT;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(length = 500)
    private String fileUrl;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    private Note note;
    
    @Column(nullable = false)
    private Boolean isRead = false;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
