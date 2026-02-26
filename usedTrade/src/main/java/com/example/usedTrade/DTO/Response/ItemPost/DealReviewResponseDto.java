package com.example.usedTrade.DTO.Response.ItemPost;

import com.example.usedTrade.Entity.Item.Deal;
import com.example.usedTrade.Entity.Item.ItemPostImage;
import com.example.usedTrade.Entity.Item.Review;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Getter
public class DealReviewResponseDto {

    private Long dealId;
    private Long itemPostId;
    private String title;
    private int price;
    private String thumbnailUrl;

    private String buyerNickname;
    private String reviewContent;
    private LocalDateTime reviewCreatedAt;

    public DealReviewResponseDto(Deal deal, Review review) {
        this.dealId = deal.getId();
        this.itemPostId = deal.getItemPost().getId();
        this.title = deal.getItemPost().getTitle();
        this.price = deal.getItemPost().getPrice();
        this.thumbnailUrl = deal.getItemPost()
                .getItemPostImageList()
                .stream()
                .map(ItemPostImage::getUrl)
                .findFirst()
                .orElse(null);

        this.buyerNickname = deal.getBuyer().getNickname();
        if (review != null) {
            this.reviewContent = review.getContent();
            this.reviewCreatedAt = review.getCreatedAt();
        }

    }
}
