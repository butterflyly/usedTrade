package com.example.usedTrade.DTO.Response.ItemPost;

import com.example.usedTrade.Entity.Item.Deal;
import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Item.Review;
import com.example.usedTrade.Entity.Users.Users;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReceivedReviewDto {

    private Long dealId;
    private Long reviewId;

    private Long itemPostId;
    private String itemTitle;

    private String reviewerUsername;   // 구매자
    private String reviewerNickname;

    private int rating;
    private String content;
    private LocalDateTime createdAt;
/*
    public ReceivedReviewDto(Deal deal) {
        Review review = deal.getReview();
        Users reviewer = review.getBuyer(); // 리뷰는 구매자가 적었겠지
        ItemPost itemPost = deal.getItemPost();

        this.dealId = deal.getId();
        this.reviewId = review.getId();

        this.itemPostId = itemPost.getId();
        this.itemTitle = itemPost.getTitle();

        this.reviewerUsername = reviewer.getUsername();
        this.reviewerNickname = reviewer.getNickname();

        this.rating = review.getRating();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
    }


 */
}