// package com.notex.repository;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import com.notex.model.entity.User;

// /**
//  * UserRepository - Database operations for User entity
//  * 
//  * @Repository tells Spring this is a repository
//  * JpaRepository provides built-in methods like:
//  *   - save()
//  *   - findById()
//  *   - findAll()
//  *   - delete()
//  *   - etc.
//  * 
//  * JpaRepository<User, Long> means:
//  *   - We're working with User entity
//  *   - The ID type is Long
//  * 
//  * Spring Data JPA automatically implements these methods!
//  * You just declare the method signature, Spring creates the SQL
//  */
// @Repository
// public interface UserRepository extends JpaRepository<User, Long> {
    
//     /**
//      * Find user by username
//      * Spring automatically creates: SELECT * FROM users WHERE username = ?
//      * 
//      * Optional means the result might be null
//      * Always use Optional when you're not sure if data exists
//      */
//     Optional<User> findByUsername(String username);
    
//     /**
//      * Find user by email
//      * SQL: SELECT * FROM users WHERE email = ?
//      */
//     Optional<User> findByEmail(String email);
    
//     /**
//      * Find user by username OR email
//      * SQL: SELECT * FROM users WHERE username = ? OR email = ?
//      * 
//      * This is useful for login (user can enter username or email)
//      */
//     Optional<User> findByUsernameOrEmail(String username, String email);
    
//     /**
//      * Check if username exists
//      * SQL: SELECT COUNT(*) > 0 FROM users WHERE username = ?
//      * Returns true if username exists, false if not
//      */
//     Boolean existsByUsername(String username);
    
//     /**
//      * Check if email exists
//      * SQL: SELECT COUNT(*) > 0 FROM users WHERE email = ?
//      */
//     Boolean existsByEmail(String email);

//     List<User> findByUsernameContainingIgnoreCaseOrDisplayNameContainingIgnoreCase(String username, String displayName);
// }

//New Code -
package com.notex.repository;

import com.notex.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String email);
    
    List<User> findByIsOnlineTrue();
    
    @Modifying
    @Query("UPDATE User u SET u.isOnline = :status, u.lastSeen = :lastSeen WHERE u.id = :userId")
    void updateOnlineStatus(@Param("userId") Long userId, 
                           @Param("status") Boolean status,
                           @Param("lastSeen") LocalDateTime lastSeen);
}