// package com.notex.repository;

// import com.notex.model.entity.Friendship;
// import com.notex.model.entity.User;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;
// import org.springframework.data.jpa.repository.Modifying;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

// import java.util.List;
// import java.util.Optional;

// import com.notex.model.enums.FriendshipStatus;

// @Repository
// public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    
//     /**
//      * Find friendship between two users
//      */
//     Optional<Friendship> findByUserIdAndFriendId(Long userId, Long friendId);
    
//     /**
//      * Find all accepted friends of a user
//      */
//     List<Friendship> findByUserIdAndStatus(Long userId, FriendshipStatus status);
    
//     /**
//      * Find all pending friend requests for a user
//      */
//     List<Friendship> findByFriendIdAndStatus(Long friendId, FriendshipStatus status);
    
//     /**
//      * Get all friends (both directions)
//      * A friendship goes both ways, so we need to check both user_id and friend_id
//      */
//     @Query("SELECT f FROM Friendship f WHERE " +
//            "(f.userId = :userId OR f.friendId = :userId) " +
//            "AND f.status = :status")
//     List<Friendship> findAllFriendships(
//         @Param("userId") Long userId,
//         @Param("status") FriendshipStatus status
//     );

//     // persistence method for a friendship request - service can build entity and save, but keep a convenience method
//     default void createRequest(Long fromUserId, Long toUserId) {
//         // default no-op placeholder: prefer constructing Friendship in service and calling save(...)
//         throw new UnsupportedOperationException("createRequest is a convenience helper - construct Friendship and call save(...) instead");
//     }

//     @Modifying
//     @Transactional
//     @Query("update Friendship f set f.status = 'ACCEPTED' where f.id = :requestId")
//     void acceptRequest(@Param("requestId") Long requestId);

//     @Modifying
//     @Transactional
//     @Query("update Friendship f set f.status = 'DECLINED' where f.id = :requestId")
//     void declineRequest(@Param("requestId") Long requestId);

//     // find friends (assumes repository has JPQL implementation or Spring Data can derive)
//     List<User> findFriendsByUserId(Long userId);

//     // find pending incoming requests for a user
//     List<User> findPendingRequests(Long userId);

//     // existence check for friendship relation
//     boolean existsFriendship(Long userId, Long otherUserId);
// }

//New Code -
package com.notex.repository;

import com.notex.model.entity.Friendship;
import com.notex.model.enums.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    
    Optional<Friendship> findByUserIdAndFriendId(Long userId, Long friendId);
    
    List<Friendship> findByUserIdAndStatus(Long userId, FriendshipStatus status);
    
    List<Friendship> findByFriendIdAndStatus(Long friendId, FriendshipStatus status);
    
    @Query("SELECT f FROM Friendship f WHERE (f.userId = :userId OR f.friendId = :userId) AND f.status = :status")
    List<Friendship> findAllFriendships(@Param("userId") Long userId, @Param("status") FriendshipStatus status);
    
    Boolean existsByUserIdAndFriendId(Long userId, Long friendId);
}
