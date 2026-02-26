package com.example.usedTrade.Entity.Alarm;

import com.example.usedTrade.Entity.Item.Review;
import com.example.usedTrade.Entity.Users.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_user_id", nullable = false)
    private Users receiver;


    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String message;
    private String targetUrl;

    @Column(name = "is_read")
    private boolean read;

    private LocalDateTime createdAt;

    public static Notification create(
            Users receiver,
            NotificationType type,
            String message,
            String targetUrl
    ) {
        Notification n = new Notification();
        n.receiver = receiver;
        n.type = type;
        n.message = message;
        n.targetUrl = targetUrl;
        n.read = false;
        n.createdAt = LocalDateTime.now();
        return n;
    }

    public void markAsRead() {
        this.read = true;
    }
}
