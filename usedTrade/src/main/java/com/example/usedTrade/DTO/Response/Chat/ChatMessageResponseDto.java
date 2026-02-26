package com.example.usedTrade.DTO.Response.Chat;

import com.example.usedTrade.Entity.Chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponseDto {

    private Long messageId;
    private Long roomId;

    private Long senderId;
    private String senderNickname;

    private String content;
    private LocalDateTime createdAt;

    public static ChatMessageResponseDto from(ChatMessage message) {
        return ChatMessageResponseDto.builder()
                .messageId(message.getId())
                .roomId(message.getChatRoom().getId())
                .senderId(message.getSender().getId())
                .senderNickname(message.getSender().getNickname())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
