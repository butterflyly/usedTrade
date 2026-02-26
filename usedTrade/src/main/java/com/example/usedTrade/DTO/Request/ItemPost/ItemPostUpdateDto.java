package com.example.usedTrade.DTO.Request.ItemPost;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPostUpdateDto {

    private String title;
    private int price;
    private String content;
}