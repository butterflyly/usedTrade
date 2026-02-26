package com.example.usedTrade.DTO.Response.ItemPost;

import com.example.usedTrade.DTO.Response.Users.UserResponseDto;
import com.example.usedTrade.Entity.Item.ItemLike;
import com.example.usedTrade.Entity.Item.ItemPostImage;
import com.example.usedTrade.Entity.Users.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class ItemLikeResponseDto {

    private Long id;
    private UserResponseDto users;
    private LocalDateTime createdAt;
    private String itemTitle;
    private int itemPrice;
    private Long itemId;
    private List<String> itemImageUrl;

    public ItemLikeResponseDto(ItemLike itemLike)
    {
        this.id = itemLike.getId();
        this.createdAt = itemLike.getCreatedAt();
        Users user = itemLike.getUsers();

        if(user != null)
        {
            this.users = UserResponseDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                    .displayNickname(user.getDisplayNickname())
                    .build();
        }

        this.itemTitle = itemLike.getItemPost().getTitle();
        this.itemPrice = itemLike.getItemPost().getPrice();
        this.itemId = itemLike.getItemPost().getId();
        this.itemImageUrl = itemLike.getItemPost().getItemPostImageList().stream().
                map(ItemPostImage::getUrl).collect(Collectors.toList());
    }

}

