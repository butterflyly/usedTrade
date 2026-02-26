package com.example.usedTrade.DTO.Request.ItemPost;

import com.example.usedTrade.Entity.Item.Deal;
import com.example.usedTrade.Entity.Item.ItemCategory;
import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Item.Review;
import com.example.usedTrade.Entity.Users.Users;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.example.usedTrade.Entity.Item.Enum.ItemStatus.ON_SALE;

@Getter
@Setter
public class ReviewCreateRequest {
    private Long dealId;
    private String content;
    private int rating;

    public Review toEntity(Deal deal) {
        return Review.builder().
                deal(deal).
                content(content).rating(rating).
                build();
    }
}