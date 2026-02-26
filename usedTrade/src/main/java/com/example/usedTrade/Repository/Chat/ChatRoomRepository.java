package com.example.usedTrade.Repository.Chat;

import com.example.usedTrade.Entity.Chat.ChatRoom;
import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Users.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    Optional<ChatRoom> findByPostIdAndBuyerId(Long postId, Long buyerId);

    Page<ChatRoom> findBySellerOrBuyer(Users seller, Users buyer, Pageable pageable);

    Optional<ChatRoom> findByPostAndSellerAndBuyer(
            ItemPost post,
            Users seller,
            Users buyer
    );

}
