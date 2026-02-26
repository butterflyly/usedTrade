package com.example.usedTrade.DTO.Response.ItemPost;

import com.example.usedTrade.Entity.Item.Deal;
import com.example.usedTrade.Entity.Item.Enum.DealStatus;
import com.example.usedTrade.Entity.Item.ItemPostImage;
import lombok.Getter;

import java.util.stream.Collectors;

@Getter
public class DealBuyResponseDto {

    private Long dealId;
    private Long itemPostId;
    private String title;
    private int price;
    private String thumbnailUrl;

    private String sellerNickname;
    private DealStatus dealStatus;

    public DealBuyResponseDto(Deal deal) {
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

        this.sellerNickname = deal.getSeller().getNickname();
        this.dealStatus = deal.getStatus();
    }
}