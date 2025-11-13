// package com.notex.model.entity;

// // JPA annotations for database mapping
// import java.time.LocalDateTime;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.PrePersist;
// import jakarta.persistence.PreUpdate;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// /**
//  * User Entity - Represents a user in the database
//  * This class maps to the 'users' table in MySQL
//  * 
//  * @Entity tells JPA this is a database table
//  * @Table specifies the table name
//  * @Data is Lombok - auto-generates getters, setters, toString, equals, hashCode
//  * @NoArgsConstructor creates a constructor with no parameters
//  * @AllArgsConstructor creates a constructor with all parameters
//  */
// @Entity
// @Table(name = "users")
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// public class User {
    
//     /**
//      * @Id marks this as the primary key
//      * @GeneratedValue tells database to auto-generate this value
//      * GenerationType.IDENTITY means use AUTO_INCREMENT in MySQL
//      */
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;
    
//     /**
//      * @Column configures the database column
//      * unique = true means no two users can have same username
//      * nullable = false means this field is required (NOT NULL in SQL)
//      */
//     @Column(unique = true, nullable = false, length = 50)
//     private String username;

//     // password field already used by services
//     @Column(nullable = false)
//     private String password;

//     // Added: displayName used by services (getDisplayName / setDisplayName)
//     @Column(name = "display_name")
//     private String displayName;

//     // Added: email used by services (getEmail / setEmail)
//     @Column(name = "email")
//     private String email;

//     // existing fields (roles, timestamps, etc.) — keep as-is if present
//     private LocalDateTime createdAt;
//     private LocalDateTime updatedAt;

//     @PrePersist
//     protected void onCreate() {
//         this.createdAt = LocalDateTime.now();
//     }

//     @PreUpdate
//     protected void onUpdate() {
//         this.updatedAt = LocalDateTime.now();
//     }
// }


//New code -
package com.notex.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(nullable = false, length = 255)
    private String passwordHash;
    
    @Column(length = 100)
    private String fullName;
    
    @Column(columnDefinition = "TEXT")
    private String bio;
    
    @Column(length = 500)
    private String profilePictureUrl;
    
    @Column(nullable = false)
    private Boolean isPublic = true;
    
    @Column(nullable = false)
    private Boolean isOnline = false;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private LocalDateTime lastSeen;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        lastSeen = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}