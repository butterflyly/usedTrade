package com.example.usedTrade.DTO.Request.ItemPost;

import com.example.usedTrade.Entity.Item.ItemCategory;
import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Users.Users;
import lombok.*;

import java.time.LocalDateTime;

import static com.example.usedTrade.Entity.Item.Enum.ItemStatus.ON_SALE;

@Getter
@Setter
public class ItemPostRequestDto {

    private String title;
    private int price;
    private String content;
    private Long categoryId;

    public ItemPostRequestDto(String title, int price, String content, Long categoryId) {
        this.title = title;
        this.price = price;
        this.content = content;
        this.categoryId = categoryId;
    }

    public ItemPost toEntity(Users user, ItemCategory category) {
        return ItemPost.builder().
                title(title).price(price).
                content(content).createdAt(LocalDateTime.now()).seller(user).status(ON_SALE).
                category(category).
                build();
    }
}