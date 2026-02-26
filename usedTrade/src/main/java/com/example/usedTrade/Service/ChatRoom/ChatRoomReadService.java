package com.example.usedTrade.Service.ChatRoom;

import com.example.usedTrade.DTO.Response.Chat.ChatRoomResponseDto;
import com.example.usedTrade.Entity.Chat.ChatRoom;
import com.example.usedTrade.Entity.Users.Users;
import com.example.usedTrade.Repository.Chat.ChatMessageRepository;
import com.example.usedTrade.Repository.Chat.ChatRoomRepository;
import com.example.usedTrade.Repository.Users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomReadService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatRoom findRoomByPostAndUsers(Long postId, String username) {

        Users users = userRepository.findByUsernameAndDeletedFalse(username).orElseThrow(()->
                new RuntimeException("유저가 존재하지않아요"));

        Optional<ChatRoom> chatRoom = chatRoomRepository.findByPostIdAndBuyerId(postId, users.getId());

        return chatRoom.orElse(null);
    }


    // 채팅방 검증 로직
    public ChatRoom validateAccess(Long roomId, String username) {

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 없음"));

        Long userId = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow().getId();

        boolean isSeller = room.getSeller().getId().equals(userId);
        boolean isBuyer  = room.getBuyer().getId().equals(userId);

        if (!isSeller && !isBuyer) {
            throw new AccessDeniedException("채팅방 접근 권한 없음");
        }

        return room;
    }

    /**
     * 현재 사용자 기준 상대방 반환
     */
    public Users getOpponent(ChatRoom room, String currentUsername) {

        if (room.getSeller().getUsername().equals(currentUsername)) {
            return room.getBuyer();
        }

        if (room.getBuyer().getUsername().equals(currentUsername)) {
            return room.getSeller();
        }

        // validateAccess를 통과했다면 사실상 여기 올 일은 없음
        throw new IllegalStateException("채팅방 참여자가 아닙니다.");
    }


    public Page<ChatRoomResponseDto> getMyChatRooms(String username, Pageable pageable) {

        Users me = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));

        Page<ChatRoom> chatRooms =
                chatRoomRepository.findBySellerOrBuyer(me, me, pageable);

        return chatRooms.map(chatRoom -> {

            int unreadCount =
                    chatMessageRepository.countUnreadByRoomAndUser(chatRoom, me);

            return new ChatRoomResponseDto(chatRoom, me, unreadCount);
        });
    }

    public int getUnreadChatRoomCount(String username) {
        Users user = userRepository
                .findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        return (int) chatMessageRepository.countUnreadChatRooms(user);
    }
}
