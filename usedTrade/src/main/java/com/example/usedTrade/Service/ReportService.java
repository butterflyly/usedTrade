package com.example.usedTrade.Service;

import com.example.usedTrade.DTO.Request.ReportCreateRequest;
import com.example.usedTrade.DTO.Response.ReportResponseDTO;
import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Report;
import com.example.usedTrade.Entity.ReportReason;
import com.example.usedTrade.Entity.ReportStatus;
import com.example.usedTrade.Entity.Users.UserStatus;
import com.example.usedTrade.Entity.Users.Users;
import com.example.usedTrade.Repository.ItemPost.ItemPostRepository;
import com.example.usedTrade.Repository.ReportRepository;
import com.example.usedTrade.Repository.Users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.usedTrade.Entity.ReportReason.SCAM;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final ItemPostRepository itemPostRepository;
    private final MessageService messageService;

    @Transactional
    public void create(ReportCreateRequest request, String username) {

        // 신고자
        Users reporter = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        // 신고 대상 게시글
        ItemPost itemPost = itemPostRepository.findById(request.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        // 게시글 작성자
        Users targetUser = itemPost.getSeller();

        Report report = Report.builder()
                .reporter(reporter)
                .itemPost(itemPost)
                .targetUser(targetUser)
                .reason(request.getReason())
                .content(request.getContent())
                .status(ReportStatus.RECEIVED)
                .createdAt(LocalDateTime.now())
                .build();

        reportRepository.save(report);
    }

    public Page<ReportResponseDTO> reportPage(Pageable pageable, ReportReason reason)
    {
        Page<Report> reportPage = reportRepository.findByReason(pageable,reason);
        return reportPage.map(ReportResponseDTO::new);
    }

    public ReportReason reportReason(String reportReason) {
        try {
            return ReportReason.valueOf(reportReason.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 신고 사유입니다.");
            // 혹은 CustomException + 400 처리
        }
    }

    public ReportResponseDTO reportDetail(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("신고 내역이 존재하지 않습니다."));
        return new ReportResponseDTO(report);
    }

    /**
     * 신고 승인(처리 완료)
     */
    public void resolveReport(Long reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 없음"));

        if (report.getStatus() == ReportStatus.RESOLVED) {
            return;
        }

        Users target = report.getTargetUser();

        // 1️⃣ 신고 처리 완료
        report.resolve();

        // 2️⃣ 신고 누적
        target.increaseReportCount();

        // 3️⃣ 제재 판단 + 대상자 메시지
        applyPenalty(target);

        // 4️⃣ 신고자에게 결과 통보
        messageService.send(
                report.getReporter(),
                "신고하신 건이 처리되어 조치가 완료되었습니다."
        );
    }

    /**
     * 신고 반려
     */
    public void rejectReport(Long reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 없음"));

        if (report.getStatus() == ReportStatus.RESOLVED) {
            return;
        }

        report.reject();

        messageService.send(
                report.getReporter(),
                "신고하신 건은 반려되었습니다."
        );
    }

    /**
     * 누적 신고 수에 따른 제재 처리
     */
    private void applyPenalty(Users user) {

        int count = user.getReportCount();

        if (user.shouldBeBlack()) {
            user.black();
            messageService.send(
                    user,
                    "신고 누적으로 계정이 블랙 처리되었습니다."
            );
        } else {
            user.warn();
            messageService.send(
                    user,
                    "신고로 인해 경고 조치되었습니다. (누적 " + count + "회)"
            );
        }
    }

    public void processReport(Long id) {

    }
}
