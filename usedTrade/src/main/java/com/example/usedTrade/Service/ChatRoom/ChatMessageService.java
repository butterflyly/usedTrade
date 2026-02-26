package com.example.usedTrade.Service.ChatRoom;

import com.example.usedTrade.DTO.Request.Chat.ChatMessageRequestDto;
import com.example.usedTrade.DTO.Response.Chat.ChatMessageResponseDto;
import com.example.usedTrade.Entity.Alarm.NotificationType;
import com.example.usedTrade.Entity.Chat.ChatMessage;
import com.example.usedTrade.Entity.Chat.ChatRoom;
import com.example.usedTrade.Entity.Users.Users;
import com.example.usedTrade.Repository.Chat.ChatMessageRepository;
import com.example.usedTrade.Repository.Chat.ChatRoomRepository;
import com.example.usedTrade.Repository.Users.UserRepository;
import com.example.usedTrade.Service.Alram.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    @Transactional
    public void send(ChatMessageRequestDto dto, String username) {

        ChatRoom room = chatRoomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방 없음"));

        Users sender = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));


        // 메시지 저장
        ChatMessage message = ChatMessage.builder()
                .chatRoom(room)
                .sender(sender)
                .content(dto.getContent())
                .read(false)
                .build();

        chatMessageRepository.save(message);

        // 응답 DTO
        ChatMessageResponseDto response =
                ChatMessageResponseDto.from(message);

        // 🔥 구독 중인 모든 클라이언트에게 전송
        messagingTemplate.convertAndSend(
                "/sub/chat/" + room.getId(),
                response
        );

        Users receiver;

        if (room.getSeller().getUsername().equals(username)) {
            receiver = room.getBuyer();
        } else if (room.getBuyer().getUsername().equals(username)) {
            receiver = room.getSeller();
        } else {
            throw new IllegalStateException("채팅방 참여자가 아닙니다.");
        }

        notificationService.notify(
                receiver,
                NotificationType.CHAT,
                sender.getUsername() + "님이 메시지를 보냈습니다.",
                "/chats/" + room.getId());
    }

    @Transactional
    public List<ChatMessageResponseDto> getMessages(
            Long roomId,
            String username
    ) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 없음"));

        // 🔐 접근 검증 (난입 방지)
        if (!room.isParticipant(username)) {
            throw new AccessDeniedException("채팅방 접근 권한 없음");
        }

        // 🔥 여기서 읽음 처리
        chatMessageRepository.markAsRead(roomId, username);

        return chatMessageRepository
                .findByChatRoomIdOrderByCreatedAtAsc(roomId)
                .stream()
                .map(ChatMessageResponseDto::from)
                .toList();
    }


}
