package com.example.usedTrade.DTO.Response.ItemPost;

import com.example.usedTrade.DTO.Response.Users.UserResponseDto;
import com.example.usedTrade.Entity.Item.Deal;
import com.example.usedTrade.Entity.Item.ItemLike;
import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Item.ItemPostImage;
import com.example.usedTrade.Entity.Item.Enum.ItemStatus;
import com.example.usedTrade.Entity.Users.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class ItemPostResponseDto {
    private Long id;
    private String title;
    private int price;
    private String content;
    private LocalDateTime createdAt;
    private String createdAtFormatted;
    private String sellerDisplayName;
    private int views;
    private UserResponseDto seller;
    private ItemStatus itemStatus;
    // 이부분 추가
    private List<String> imageUrls;

    @Builder.Default
    private List<Long> itemLikesUserId = new ArrayList<>();
    private Long categoryId;
    private List<ItemPostImage> image;
    private String sellerNickname;
    private Long dealId;     // 현재 유저 기준 Deal ID
    private boolean hasDeal; // Deal 존재 여부

    public void setDeal(Deal deal) {
        if (deal != null) {
            this.dealId = deal.getId();
            this.hasDeal = true;
        } else {
            this.hasDeal = false;
        }
    }

    public static ItemPostResponseDto from(ItemPost itemPost) {
        return ItemPostResponseDto.builder()
                .id(itemPost.getId())
                .title(itemPost.getTitle())
                .price(itemPost.getPrice())
                .itemStatus(itemPost.getStatus())
                .createdAtFormatted(
                        itemPost.getCreatedAt()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                )
                .build();
    }


    public ItemPostResponseDto(ItemPost itemPost) {
        this.id = itemPost.getId();
        this.title = itemPost.getTitle();
        this.price = itemPost.getPrice();
        this.content = itemPost.getContent();
        this.createdAt = itemPost.getCreatedAt();

        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(itemPost.getCreatedAt(), now);

        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (minutes < 1) {
            this.createdAtFormatted = "방금 전";
        } else if (minutes < 60) {
            this.createdAtFormatted = minutes + "분 전";
        } else if (hours < 24) {
            this.createdAtFormatted = hours + "시간 전";
        } else if (days < 7) {
            this.createdAtFormatted = days + "일 전";
        } else {
            this.createdAtFormatted = itemPost.getCreatedAt()
                    .format(DateTimeFormatter.ofPattern(
                            "yyyy년 MM월 dd일 a hh시 mm분", Locale.KOREA));
        }

        this.sellerDisplayName = itemPost.getSeller() != null ? itemPost.getSeller().getDisplayNickname() : "익명";
        this.views = itemPost.getViews();
        this.itemStatus = itemPost.getStatus();
        this.imageUrls = itemPost.getItemPostImageList().stream().
                map(ItemPostImage::getUrl).collect(Collectors.toList());

        // 엔티티 → DTO로 변경
        Users seller = itemPost.getSeller();
        if (seller != null) {
            this.seller = UserResponseDto.builder()
                    .id(seller.getId())
                    .username(seller.getUsername())
                    .nickname(seller.getNickname())
                    .displayNickname(seller.getDisplayNickname())
                   // .profileImagePath(seller.getProfileImagePath())
                  //  .rating(seller.getRating())
                    .build();

            this.sellerNickname = seller.getDisplayNickname();
        }

        this.categoryId = itemPost.getCategory().getId();

        Set<ItemLike> likes = itemPost.getItemLikes();

        if (likes != null) {
            this.itemLikesUserId = likes.stream()
                    .map(like -> like.getUsers().getId())
                    .toList();
        } else {
            this.itemLikesUserId = new ArrayList<>();
        }

        this.image = itemPost.getItemPostImageList();
    }

}