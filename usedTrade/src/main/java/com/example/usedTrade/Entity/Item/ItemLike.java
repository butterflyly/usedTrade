package com.example.usedTrade.Entity.Item;

import com.example.usedTrade.Entity.Users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "item_like",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"item_post_id", "users_id"})
        }
)
public class ItemLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_post_id")
    private ItemPost itemPost;

    private LocalDateTime createdAt;

    protected void setItemPost(ItemPost itemPost) {
        this.itemPost = itemPost;
    }
}