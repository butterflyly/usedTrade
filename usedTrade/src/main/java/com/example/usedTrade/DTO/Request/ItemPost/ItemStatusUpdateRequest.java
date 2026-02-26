package com.example.usedTrade.DTO.Request.ItemPost;


import com.example.usedTrade.Entity.Item.Enum.ItemStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemStatusUpdateRequest {

    private ItemStatus Status;
}
