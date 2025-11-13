// package com.notex.service;

// import org.springframework.stereotype.Service;
// import org.springframework.beans.factory.annotation.Autowired;
// import com.notex.model.entity.ChatMessage;
// import com.notex.repository.ChatMessageRepository;

// import java.util.List;
// import java.util.Optional;

// @Service
// public class ChatService {

//     @Autowired
//     private ChatMessageRepository chatMessageRepository;

//     public ChatMessage sendMessage(ChatMessage message) {
//         return chatMessageRepository.save(message);
//     }

//     public List<ChatMessage> getMessagesByGroup(Long groupId, int page, int size) {
//         return chatMessageRepository.findByGroupIdOrderByCreatedAtDesc(groupId);
//     }

//     public List<ChatMessage> getRecentMessagesForUser(String username, int limit) {
//         return chatMessageRepository.findRecentForUser(username, limit);
//     }

//     public Optional<ChatMessage> findById(Long id) {
//         return chatMessageRepository.findById(id);
//     }
// }

//New Code -

package com.notex.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import com.notex.model.entity.ChatMessage;
import com.notex.model.entity.Group;
import com.notex.model.entity.User;
import com.notex.model.entity.Note;
import com.notex.model.dto.request.SendMessageRequest;
import com.notex.model.dto.response.ChatMessageResponse;
import com.notex.model.dto.response.PageResponse;
import com.notex.model.dto.response.UserResponse;
import com.notex.repository.ChatMessageRepository;
import com.notex.repository.GroupRepository;
import com.notex.repository.GroupMemberRepository;
import com.notex.repository.UserRepository;
import com.notex.repository.NoteRepository;
import com.notex.exception.ResourceNotFoundException;
import com.notex.exception.UnauthorizedException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Transactional
    public ChatMessageResponse sendMessage(Long groupId, SendMessageRequest request, String username) {
        User sender = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        
        if (!groupMemberRepository.existsByGroupIdAndUserId(groupId, sender.getId())) {
            throw new UnauthorizedException("You are not a member of this group");
        }
        
        ChatMessage message = new ChatMessage();
        message.setGroup(group);
        message.setSender(sender);
        message.setMessageType(request.getMessageType());
        message.setContent(request.getContent());
        message.setFileUrl(request.getFileUrl());
        
        if (request.getNoteId() != null) {
            Note note = noteRepository.findById(request.getNoteId())
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
            message.setNote(note);
        }
        
        ChatMessage savedMessage = chatMessageRepository.save(message);
        return convertToResponse(savedMessage);
    }
    
    public PageResponse<ChatMessageResponse> getGroupMessages(Long groupId, int page, int size, Long userId) {
        if (!groupMemberRepository.existsByGroupIdAndUserId(groupId, userId)) {
            throw new UnauthorizedException("You are not a member of this group");
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ChatMessage> messagesPage = chatMessageRepository.findByGroupIdOrderByCreatedAtDesc(groupId, pageable);
        
        return convertToPageResponse(messagesPage);
    }
    
    public List<ChatMessageResponse> getRecentMessages(Long groupId, Long userId) {
        if (!groupMemberRepository.existsByGroupIdAndUserId(groupId, userId)) {
            throw new UnauthorizedException("You are not a member of this group");
        }
        
        return chatMessageRepository.findTop50ByGroupIdOrderByCreatedAtDesc(groupId)
            .stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public void markAsRead(Long messageId, Long userId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
            .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        
        if (!groupMemberRepository.existsByGroupIdAndUserId(message.getGroup().getId(), userId)) {
            throw new UnauthorizedException("You are not a member of this group");
        }
        
        message.setIsRead(true);
        chatMessageRepository.save(message);
    }
    
    public Long getUnreadCount(Long groupId, Long userId) {
        if (!groupMemberRepository.existsByGroupIdAndUserId(groupId, userId)) {
            throw new UnauthorizedException("You are not a member of this group");
        }
        
        return chatMessageRepository.countUnreadMessagesByGroupId(groupId);
    }
    
    private ChatMessageResponse convertToResponse(ChatMessage message) {
        ChatMessageResponse response = new ChatMessageResponse();
        response.setId(message.getId());
        response.setGroupId(message.getGroup().getId());
        response.setSender(modelMapper.map(message.getSender(), UserResponse.class));
        response.setMessageType(message.getMessageType());
        response.setContent(message.getContent());
        response.setFileUrl(message.getFileUrl());
        
        if (message.getNote() != null) {
            response.setNoteId(message.getNote().getId());
        }
        
        response.setIsRead(message.getIsRead());
        response.setCreatedAt(message.getCreatedAt());
        
        return response;
    }
    
    private PageResponse<ChatMessageResponse> convertToPageResponse(Page<ChatMessage> messagesPage) {
        List<ChatMessageResponse> content = messagesPage.getContent()
            .stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
        
        PageResponse<ChatMessageResponse> response = new PageResponse<>();
        response.setContent(content);
        response.setPageNumber(messagesPage.getNumber());
        response.setPageSize(messagesPage.getSize());
        response.setTotalElements(messagesPage.getTotalElements());
        response.setTotalPages(messagesPage.getTotalPages());
        response.setLast(messagesPage.isLast());
        response.setFirst(messagesPage.isFirst());
        
        return response;
    }
}