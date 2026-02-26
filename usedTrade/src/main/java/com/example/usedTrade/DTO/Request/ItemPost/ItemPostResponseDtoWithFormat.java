package com.example.usedTrade.DTO.Request.ItemPost;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemPostResponseDtoWithFormat {
    private Long id;
    private String title;
    private int price;
    private String content;
    private String formattedCreatedAt; // 포맷된 문자열
}