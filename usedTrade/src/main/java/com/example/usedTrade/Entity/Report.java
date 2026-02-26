package com.example.usedTrade.Entity;

import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Users.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 신고한 사람
    @ManyToOne(fetch = FetchType.LAZY)
    private Users reporter;

    // 신고된 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    private ItemPost itemPost;

    // 게시글 작성자 (중복이지만 필수)
    @ManyToOne(fetch = FetchType.LAZY)
    private Users targetUser;

    // 신고 사유
    @Enumerated(EnumType.STRING)
    private ReportReason reason;
    // 욕설, 사기, 스팸, 기타

    // 상세 내용
    @Column(length = 1000)
    private String content;

    // 신고 상태
    @Enumerated(EnumType.STRING)
    private ReportStatus status;
    // RECEIVED, IN_PROGRESS, RESOLVED

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private ReportResult result;

    public void resolve() {
        this.status = ReportStatus.RESOLVED;
    }

    public void reject() {
        this.result = ReportResult.REJECTED;
    }
}