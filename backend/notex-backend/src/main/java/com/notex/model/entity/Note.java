// package com.notex.model.entity;

// import jakarta.persistence.Entity;
// import jakarta.persistence.Table;
// import jakarta.persistence.Id;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Column;
// import jakarta.persistence.Enumerated;
// import jakarta.persistence.EnumType;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToMany;
// import jakarta.persistence.CascadeType;
// import jakarta.persistence.JoinTable;

// import java.time.LocalDateTime;
// import java.util.Set;

// import com.notex.model.enums.NoteType;
// import com.notex.model.enums.Visibility;

// import java.util.HashSet;

// import lombok.Data;

// @Entity
// @Table(name = "notes")
// @Data
// public class Note {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;
    
//     @Column(nullable = false)
//     private String title;
    
//     @Column(columnDefinition = "LONGTEXT")
//     private String content;
    
//     @Enumerated(EnumType.STRING)
//     private NoteType noteType;
    
//     @Enumerated(EnumType.STRING)
//     private Visibility visibility;
    
//     private String fileUrl;
//     private String thumbnailUrl;
    
//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "created_by")
//     private User createdBy;
    
//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "group_id")
//     private Group group;
    
//     @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//     @JoinTable(
//         name = "note_tags",
//         joinColumns = @JoinColumn(name = "note_id"),
//         inverseJoinColumns = @JoinColumn(name = "tag_id")
//     )
//     private Set<Tag> tags = new HashSet<>();
    
//     private Integer viewCount = 0;
//     private Integer likeCount = 0;
    
//     private LocalDateTime createdAt = LocalDateTime.now();
//     private LocalDateTime updatedAt = LocalDateTime.now();
// }

//New Code -
package com.notex.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.notex.model.enums.NoteType;
import com.notex.model.enums.Visibility;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoteType noteType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility = Visibility.PRIVATE;
    
    @Column(length = 500)
    private String fileUrl;
    
    @Column(length = 500)
    private String thumbnailUrl;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "note_tags",
        joinColumns = @JoinColumn(name = "note_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();
    
    @Column(nullable = false)
    private Integer viewCount = 0;
    
    @Column(nullable = false)
    private Integer likeCount = 0;
    
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