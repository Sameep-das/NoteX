// package com.notex.repository;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import com.notex.model.entity.GroupMember;

// @Repository
// public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    
//     /**
//      * Find all members of a group
//      */
//     List<GroupMember> findByGroupId(Long groupId);
    
//     /**
//      * Find specific membership
//      */
//     Optional<GroupMember> findByGroupIdAndUserId(Long groupId, Long userId);
    
//     /**
//      * Check if user is member of group
//      */
//     Boolean existsByGroupIdAndUserId(Long groupId, Long userId);
// }

//New Code -
package com.notex.repository;

import com.notex.model.entity.GroupMember;
import com.notex.model.enums.GroupRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    
    List<GroupMember> findByGroupId(Long groupId);
    
    List<GroupMember> findByUserId(Long userId);
    
    Optional<GroupMember> findByGroupIdAndUserId(Long groupId, Long userId);
    
    Boolean existsByGroupIdAndUserId(Long groupId, Long userId);
    
    List<GroupMember> findByGroupIdAndRole(Long groupId, GroupRole role);
    
    Long countByGroupId(Long groupId);
    
    void deleteByGroupIdAndUserId(Long groupId, Long userId);
}