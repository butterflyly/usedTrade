package com.example.usedTrade.DTO.Response.Alram;

import com.example.usedTrade.Entity.Alarm.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private String type;
    private String message;
    private String targetUrl;
    private LocalDateTime createdAt;

    public static NotificationResponse from(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getType().name(),
                n.getMessage(),
                n.getTargetUrl(),
                n.getCreatedAt()
        );
    }
}