// package com.notex.repository;

// import java.util.List;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;

// import com.notex.model.entity.Group;

// @Repository
// public interface GroupRepository extends JpaRepository<Group, Long> {
    
//     /**
//      * Find groups created by a user
//      */
//     List<Group> findByCreatedById(Long userId);
    
//     /**
//      * Find all groups that a user is a member of
//      * This requires a JOIN with group_members table
//      */
//     @Query("SELECT g FROM Group g JOIN g.members m WHERE m.user.id = :userId")
//     List<Group> findGroupsByMemberId(@Param("userId") Long userId);
    
//     // returns groups where a member (User) has the given username
//     List<Group> findByMembersUsername(String username);
// }

//New Code -
package com.notex.repository;

import com.notex.model.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    
    List<Group> findByCreatedById(Long userId);
    
    @Query("SELECT g FROM Group g JOIN g.members m WHERE m.user.id = :userId")
    List<Group> findGroupsByMemberId(@Param("userId") Long userId);
    
    @Query("SELECT g FROM Group g WHERE g.isPrivate = false")
    List<Group> findPublicGroups();
}