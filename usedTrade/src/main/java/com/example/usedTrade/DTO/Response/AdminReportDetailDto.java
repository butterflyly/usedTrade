package com.example.usedTrade.DTO.Response;

import com.example.usedTrade.Entity.ReportReason;
import com.example.usedTrade.Entity.ReportStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminReportDetailDto {
    private Long reportId;
    private ReportReason category;
    private String reason;
    private ReportStatus status;
    private String reporterNickname;
    private String targetInfo;
    private LocalDateTime createdAt;
    private String adminMemo;
}