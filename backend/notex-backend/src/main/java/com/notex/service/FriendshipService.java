// package com.notex.service;

// import org.springframework.stereotype.Service;
// import org.springframework.beans.factory.annotation.Autowired;
// import com.notex.repository.FriendshipRepository;
// import com.notex.model.entity.User;

// import java.util.List;
// import java.util.Optional;

// @Service
// public class FriendshipService {

//     @Autowired
//     private FriendshipRepository friendshipRepository;

//     public void sendRequest(Long fromUserId, Long toUserId) {
//         friendshipRepository.createRequest(fromUserId, toUserId);
//     }

//     public void acceptRequest(Long requestId) {
//         friendshipRepository.acceptRequest(requestId);
//     }

//     public void declineRequest(Long requestId) {
//         friendshipRepository.declineRequest(requestId);
//     }

//     public List<User> getFriends(Long userId) {
//         return friendshipRepository.findFriendsByUserId(userId);
//     }

//     public List<User> getPendingRequests(Long userId) {
//         return friendshipRepository.findPendingRequests(userId);
//     }

//     public Optional<Boolean> areFriends(Long userId, Long otherUserId) {
//         return Optional.of(friendshipRepository.existsFriendship(userId, otherUserId));
//     }
// }

//New Code -
package com.notex.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import com.notex.model.entity.Friendship;
import com.notex.model.entity.User;
import com.notex.model.dto.request.FriendRequestDto;
import com.notex.model.dto.response.FriendshipResponse;
import com.notex.model.dto.response.UserResponse;
import com.notex.model.enums.FriendshipStatus;
import com.notex.repository.FriendshipRepository;
import com.notex.repository.UserRepository;
import com.notex.exception.ResourceNotFoundException;
import com.notex.exception.BadRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {
    
    @Autowired
    private FriendshipRepository friendshipRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Transactional
    public FriendshipResponse sendFriendRequest(Long userId, FriendRequestDto request) {
        if (userId.equals(request.getFriendId())) {
            throw new BadRequestException("You cannot send friend request to yourself");
        }
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        User friend = userRepository.findById(request.getFriendId())
            .orElseThrow(() -> new ResourceNotFoundException("Friend user not found"));
        
        if (!friend.getIsPublic()) {
            throw new BadRequestException("Cannot send friend request to private account");
        }
        
        if (friendshipRepository.existsByUserIdAndFriendId(userId, request.getFriendId())) {
            throw new BadRequestException("Friend request already exists");
        }
        
        Friendship friendship = new Friendship();
        friendship.setUserId(userId);
        friendship.setFriendId(request.getFriendId());
        friendship.setStatus(FriendshipStatus.PENDING);
        
        Friendship savedFriendship = friendshipRepository.save(friendship);
        return convertToResponse(savedFriendship);
    }
    
    @Transactional
    public FriendshipResponse acceptFriendRequest(Long friendshipId, Long userId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
            .orElseThrow(() -> new ResourceNotFoundException("Friendship not found"));
        
        if (!friendship.getFriendId().equals(userId)) {
            throw new BadRequestException("You can only accept requests sent to you");
        }
        
        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new BadRequestException("Friend request is not pending");
        }
        
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        
        Friendship reverseFriendship = new Friendship();
        reverseFriendship.setUserId(userId);
        reverseFriendship.setFriendId(friendship.getUserId());
        reverseFriendship.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(reverseFriendship);
        
        Friendship updated = friendshipRepository.save(friendship);
        return convertToResponse(updated);
    }
    
    @Transactional
    public void rejectFriendRequest(Long friendshipId, Long userId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
            .orElseThrow(() -> new ResourceNotFoundException("Friendship not found"));
        
        if (!friendship.getFriendId().equals(userId)) {
            throw new BadRequestException("You can only reject requests sent to you");
        }
        
        friendshipRepository.delete(friendship);
    }
    
    @Transactional
    public void removeFriend(Long friendId, Long userId) {
        friendshipRepository.findByUserIdAndFriendId(userId, friendId)
            .ifPresent(friendshipRepository::delete);
        
        friendshipRepository.findByUserIdAndFriendId(friendId, userId)
            .ifPresent(friendshipRepository::delete);
    }
    
    public List<UserResponse> getFriends(Long userId) {
        List<Friendship> friendships = friendshipRepository.findAllFriendships(userId, FriendshipStatus.ACCEPTED);
        
        List<UserResponse> friends = new ArrayList<>();
        for (Friendship friendship : friendships) {
            Long friendUserId = friendship.getUserId().equals(userId) 
                ? friendship.getFriendId() 
                : friendship.getUserId();
            
            User friend = userRepository.findById(friendUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Friend not found"));
            
            friends.add(modelMapper.map(friend, UserResponse.class));
        }
        
        return friends;
    }
    
    public List<FriendshipResponse> getPendingRequests(Long userId) {
        return friendshipRepository.findByFriendIdAndStatus(userId, FriendshipStatus.PENDING)
            .stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    public List<UserResponse> getOnlineFriends(Long userId) {
        List<Friendship> friendships = friendshipRepository.findAllFriendships(userId, FriendshipStatus.ACCEPTED);
        
        List<UserResponse> onlineFriends = new ArrayList<>();
        for (Friendship friendship : friendships) {
            Long friendUserId = friendship.getUserId().equals(userId) 
                ? friendship.getFriendId() 
                : friendship.getUserId();
            
            User friend = userRepository.findById(friendUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Friend not found"));
            
            if (friend.getIsOnline()) {
                onlineFriends.add(modelMapper.map(friend, UserResponse.class));
            }
        }
        
        return onlineFriends;
    }
    
    private FriendshipResponse convertToResponse(Friendship friendship) {
        FriendshipResponse response = new FriendshipResponse();
        response.setId(friendship.getId());
        
        User user = userRepository.findById(friendship.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User friend = userRepository.findById(friendship.getFriendId())
            .orElseThrow(() -> new ResourceNotFoundException("Friend not found"));
        
        response.setUser(modelMapper.map(user, UserResponse.class));
        response.setFriend(modelMapper.map(friend, UserResponse.class));
        response.setStatus(friendship.getStatus());
        response.setCreatedAt(friendship.getCreatedAt());
        
        return response;
    }
}