package com.example.usedTrade.Repository.Chat;

import com.example.usedTrade.Entity.Chat.ChatMessage;
import com.example.usedTrade.Entity.Chat.ChatRoom;
import com.example.usedTrade.Entity.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage ,Long> {

    List<ChatMessage> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);

    long countByChatRoomIdAndReadFalseAndSenderNot(Long chatRoomId, Users sender);

    @Modifying
    @Query("""
    update ChatMessage m
    set m.read = true
    where m.chatRoom.id = :roomId
      and m.sender.username <> :username
      and m.read = false
    """)
    void markAsRead(@Param("roomId") Long roomId,
                    @Param("username") String username);


    @Query("""
        SELECT COUNT(DISTINCT m.chatRoom.id)
        FROM ChatMessage m
        WHERE m.sender <> :user
          AND m.read = false
          AND (m.chatRoom.seller = :user OR m.chatRoom.buyer = :user)
    """)
    long countUnreadChatRooms(@Param("user") Users user);

    @Query("""
        select count(m)
        from ChatMessage m
        where m.chatRoom = :chatRoom
          and m.sender <> :user
          and m.read = false
    """)
    int countUnreadByRoomAndUser(
            @Param("chatRoom") ChatRoom chatRoom,
            @Param("user") Users user);
}


