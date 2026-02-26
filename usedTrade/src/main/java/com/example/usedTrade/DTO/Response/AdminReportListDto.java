package com.example.usedTrade.DTO.Response;

import com.example.usedTrade.Entity.ReportReason;
import com.example.usedTrade.Entity.ReportStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminReportListDto {
    private Long reportId;
    private ReportReason category;
    private ReportStatus status;
    private String reporterNickname;
    private String targetType;
    private LocalDateTime createdAt;
}