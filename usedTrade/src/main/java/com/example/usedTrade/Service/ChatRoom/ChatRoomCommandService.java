package com.example.usedTrade.Service.ChatRoom;

import com.example.usedTrade.DTO.Response.Chat.ChatRoomResponseDto;
import com.example.usedTrade.Entity.Chat.ChatRoom;
import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Users.Users;
import com.example.usedTrade.Repository.Chat.ChatRoomRepository;
import com.example.usedTrade.Repository.ItemPost.ItemPostRepository;
import com.example.usedTrade.Repository.Users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatRoomCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ItemPostRepository itemPostRepository;



    public ChatRoomResponseDto createOrGet(Long postId, String buyerUsername) {

        ItemPost post = itemPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        Users seller = post.getSeller();
        Users buyer = userRepository.findByUsernameAndDeletedFalse(buyerUsername)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // ❗ 자기 게시글이면 채팅 불가
        if (seller.getId().equals(buyer.getId())) {
            throw new IllegalStateException("자기 자신과는 채팅할 수 없습니다.");
        }

        ChatRoom chatRoom = chatRoomRepository
                .findByPostAndSellerAndBuyer(post, seller, buyer)
                .orElseGet(() -> {
                    ChatRoom room = ChatRoom.builder()
                            .post(post)
                            .seller(seller)
                            .buyer(buyer)
                            .createdAt(LocalDateTime.now())
                            .build();

                    return chatRoomRepository.save(room);
                });

        // ✅ 이 시점에서는 unreadCount = 0
        return new ChatRoomResponseDto(chatRoom, buyer, 0);
    }

    @Transactional
    public void leaveRoom(Long roomId, String username) {

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 없음"));

        room.leave(username);
    }
/*
    @Transactional
    public void leave(Long roomId, String username) {

        // 유저가 속한 방이랑 유저이름 찾기
        ChatRoomUser cru = chatRoomUserRepository
                .findByRoomIdAndUsername(roomId, username)
                .orElseThrow();

        cru.setStatus(ChatRoomStatus.LEFT);
        cru.setLeftAt(LocalDateTime.now());

        // 시스템 메시지
        ChatMessage systemMessage = ChatMessage.system(
                cru.getUser().getNickname() + "님이 채팅방을 나갔습니다."
        );

        chatMessageRepository.save(systemMessage);

        messagingTemplate.convertAndSend(
                "/sub/chat/room/" + roomId,
                ChatMessageResponseDto.from(systemMessage)
        );
    }

 */

}
