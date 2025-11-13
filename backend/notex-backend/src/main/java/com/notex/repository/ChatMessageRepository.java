// package com.notex.repository;

// import com.notex.model.entity.ChatMessage;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import java.util.List;
// import java.util.Collections;

// @Repository
// public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
//     /**
//      * Find all messages in a group (paginated)
//      * Pagination helps when there are thousands of messages
//      */
//     Page<ChatMessage> findByGroupIdOrderByCreatedAtDesc(Long groupId, Pageable pageable);
    
//     /**
//      * Find recent messages in a group (last N messages)
//      */
//     List<ChatMessage> findTop50ByGroupIdOrderByCreatedAtDesc(Long groupId);

//     // finds messages for a group ordered by creation time desc
//     List<ChatMessage> findByGroupIdOrderByCreatedAtDesc(Long groupId);

//     // derived method used by the default helper
//     List<ChatMessage> findByGroupMembersUsernameOrderByCreatedAtDesc(String username, Pageable pageable);

//     // default helper that callers expect: findRecentForUser(username, limit)
//     default List<ChatMessage> findRecentForUser(String username, int limit) {
//         if (limit <= 0) return Collections.emptyList();
//         return findByGroupMembersUsernameOrderByCreatedAtDesc(username, Pageable.ofSize(limit));
//     }
// }

//New Code -
package com.notex.repository;

import com.notex.model.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    Page<ChatMessage> findByGroupIdOrderByCreatedAtDesc(Long groupId, Pageable pageable);
    
    List<ChatMessage> findTop50ByGroupIdOrderByCreatedAtDesc(Long groupId);
    
    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.group.id = :groupId AND m.isRead = false")
    Long countUnreadMessagesByGroupId(@Param("groupId") Long groupId);
}