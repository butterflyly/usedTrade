package com.example.usedTrade.Entity.Chat;

import com.example.usedTrade.Entity.Users.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @Enumerated(EnumType.STRING)
    private ChatRoomUserStatus status; // JOINED, LEFT

    private LocalDateTime joinedAt;
    private LocalDateTime leftAt;
}