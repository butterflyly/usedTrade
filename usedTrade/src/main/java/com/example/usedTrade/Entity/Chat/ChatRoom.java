package com.example.usedTrade.Entity.Chat;

import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(
        name = "chat_room",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_chat_room_post_buyer",
                columnNames = {"post_id", "buyer_user_id"}
        )
)
public class ChatRoom {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private ItemPost post;

    @ManyToOne
    @JoinColumn(name = "seller_user_id", nullable = false)
    private Users seller;

    @ManyToOne
    @JoinColumn(name = "buyer_user_id", nullable = false)
    private Users buyer;

    private LocalDateTime createdAt;

    // 나가기 상태
    @Builder.Default
    private boolean buyerLeft = false;

    @Builder.Default
    private boolean sellerLeft = false;

    // 논리 삭제
    @Builder.Default
    private boolean deleted = false;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public boolean isParticipant(String username) {
        return seller.getUsername().equals(username)
                || buyer.getUsername().equals(username);
    }

    public void leave(String username) {
        if (seller.getUsername().equals(username)) {
            this.sellerLeft = true;
        } else if (buyer.getUsername().equals(username)) {
            this.buyerLeft = true;
        } else {
            throw new IllegalStateException("채팅방 참여자가 아님");
        }

        if (buyerLeft && sellerLeft) {
            this.deleted = true;
        }
    }
}
