package com.example.usedTrade.DTO.Request;

import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.ReportReason;
import com.example.usedTrade.Entity.ReportTargetType;
import com.example.usedTrade.Entity.Users.Users;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportCreateRequest {

    @NotNull
    private Long targetId;        // 게시글 ID
    @NotNull
    private ReportReason reason;
    private String content;
}