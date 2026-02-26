package com.example.usedTrade.Service.Alram;

import com.example.usedTrade.DTO.Response.Alram.NotificationResponse;
import com.example.usedTrade.Entity.Alarm.Notification;
import com.example.usedTrade.Entity.Alarm.NotificationType;
import com.example.usedTrade.Entity.Users.Users;
import com.example.usedTrade.Repository.Alram.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void notify(
            Users receiver,
            NotificationType type,
            String message,
            String targetUrl
    ) {
        Notification notification =
                Notification.create(receiver, type, message, targetUrl);

        notificationRepository.save(notification);

        messagingTemplate.convertAndSend(
                "/sub/notifications/" + receiver.getId(),
                NotificationResponse.from(notification)
        );
    }

    @Transactional
    public void read(Long notificationId, String username) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("알람 없음"));

        if (!notification.getReceiver().getUsername().equals(username)) {
            throw new AccessDeniedException("권한 없음");
        }

        notification.markAsRead();
    }
}