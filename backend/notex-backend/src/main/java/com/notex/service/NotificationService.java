// package com.notex.service;

// import org.springframework.stereotype.Service;
// import org.springframework.beans.factory.annotation.Autowired;
// import com.notex.model.entity.Notification;
// import com.notex.repository.NotificationRepository;

// import java.util.List;
// import java.util.Optional;

// @Service
// public class NotificationService {

//     @Autowired
//     private NotificationRepository notificationRepository;

//     public void sendNotification(Notification notification) {
//         notificationRepository.save(notification);
//     }

//     public List<Notification> getNotificationsForUser(String username, int page, int size) {
//         return notificationRepository.findByRecipientUsernameOrderByCreatedAtDesc(username);
//     }

//     public Optional<Notification> getNotificationById(Long id) {
//         return notificationRepository.findById(id);
//     }

//     public void markAsRead(Long notificationId) {
//         notificationRepository.findById(notificationId).ifPresent(n -> {
//             n.setRead(true);
//             notificationRepository.save(n);
//         });
//     }

//     public void markAllAsRead(String username) {
//         notificationRepository.markAllAsRead(username);
//     }
// }

//New Code -

package com.notex.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import com.notex.model.entity.Notification;
import com.notex.model.entity.User;
import com.notex.model.dto.response.NotificationResponse;
import com.notex.model.dto.response.PageResponse;
import com.notex.model.enums.NotificationType;
import com.notex.repository.NotificationRepository;
import com.notex.repository.UserRepository;
import com.notex.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Transactional
    public NotificationResponse createNotification(Long userId, NotificationType type, 
                                                   String content, Long referenceId, String referenceType) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setContent(content);
        notification.setReferenceId(referenceId);
        notification.setReferenceType(referenceType);
        
        Notification savedNotification = notificationRepository.save(notification);
        return modelMapper.map(savedNotification, NotificationResponse.class);
    }
    
    public PageResponse<NotificationResponse> getUserNotifications(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notificationsPage = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        
        return convertToPageResponse(notificationsPage);
    }
    
    public List<NotificationResponse> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
            .stream()
            .map(notification -> modelMapper.map(notification, NotificationResponse.class))
            .collect(Collectors.toList());
    }
    
    public Long getUnreadCount(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }
    
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        
        if (!notification.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Notification not found");
        }
        
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }
    
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsReadByUserId(userId);
    }
    
    @Transactional
    public void deleteNotification(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        
        if (!notification.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Notification not found");
        }
        
        notificationRepository.delete(notification);
    }
    
    private PageResponse<NotificationResponse> convertToPageResponse(Page<Notification> notificationsPage) {
        List<NotificationResponse> content = notificationsPage.getContent()
            .stream()
            .map(notification -> modelMapper.map(notification, NotificationResponse.class))
            .collect(Collectors.toList());
        
        PageResponse<NotificationResponse> response = new PageResponse<>();
        response.setContent(content);
        response.setPageNumber(notificationsPage.getNumber());
        response.setPageSize(notificationsPage.getSize());
        response.setTotalElements(notificationsPage.getTotalElements());
        response.setTotalPages(notificationsPage.getTotalPages());
        response.setLast(notificationsPage.isLast());
        response.setFirst(notificationsPage.isFirst());
        
        return response;
    }
}