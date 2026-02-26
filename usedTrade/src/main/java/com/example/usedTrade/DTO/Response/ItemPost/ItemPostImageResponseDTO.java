package com.example.usedTrade.DTO.Response.ItemPost;

import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Item.ItemPostImage;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemPostImageResponseDTO {

    private Long id;
    private String url;
    private ItemPost itemPost;

    @Builder
    public ItemPostImageResponseDTO(ItemPostImage itemPostImage)
    {
        this.id = itemPostImage.getImageId();
        this.url = itemPostImage.getUrl();
        this.itemPost = itemPostImage.getItemPost();;
    }
}
