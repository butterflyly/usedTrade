package com.example.usedTrade.DTO.Response;


import com.example.usedTrade.DTO.Response.ItemPost.ItemPostResponseDto;
import com.example.usedTrade.DTO.Response.Users.UserResponseDto;
import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Report;
import com.example.usedTrade.Entity.ReportReason;
import com.example.usedTrade.Entity.ReportStatus;
import com.example.usedTrade.Entity.Users.Users;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ReportResponseDTO {

    private Long id;
    private UserResponseDto reporter;
    private ItemPostResponseDto itemPost;
    private UserResponseDto targetUser;
    private ReportReason reason;
    private String content;
    private ReportStatus status;
    private String createdAt;

    public ReportResponseDTO(Report report)
    {
        this.id = report.getId();

        // 엔티티 → DTO로 변경
        Users reporter = report.getReporter();
        if (reporter != null) {
            this.reporter = UserResponseDto.builder()
                    .id(reporter.getId())
                    .username(reporter.getUsername())
                    .nickname(reporter.getNickname())
                    .displayNickname(reporter.getDisplayNickname())
                    // .profileImagePath(seller.getProfileImagePath())
                    //  .rating(seller.getRating())
                    .build();
        }

        ItemPost itemPost1 = report.getItemPost();
        if(itemPost1 != null)
        {
            // 일단 이정도 정보만 있으면 될거같음
            this.itemPost = ItemPostResponseDto.builder().
                    id(itemPost1.getId())
                    .title(itemPost1.getTitle()).
                    build();
        }

        Users target = report.getTargetUser();
        if(target !=null)
        {
            this.targetUser =UserResponseDto.builder()
                    .id(target.getId())
                    .username(target.getUsername())
                    .nickname(target.getNickname())
                    .displayNickname(target.getDisplayNickname())

                    .build();
        }

        this.reason = report.getReason();
        this.content = report.getContent();
        this.status = report.getStatus();
        this.createdAt =  report.getCreatedAt()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));




    }

}
