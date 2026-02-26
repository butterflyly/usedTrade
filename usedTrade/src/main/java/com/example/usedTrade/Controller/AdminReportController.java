package com.example.usedTrade.Controller;

import com.example.usedTrade.DTO.Response.ReportResponseDTO;
import com.example.usedTrade.Entity.ReportReason;
import com.example.usedTrade.Service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/admin/report")
@Controller
public class AdminReportController {

    private final ReportService reportService;

    @GetMapping("/{reportReason}")
    public String reportList(@PathVariable String reportReason,
                             @RequestParam(defaultValue = "0") int page, Model model
    ) {
        ReportReason reason = reportService.reportReason(reportReason);

        Pageable pageable = PageRequest.of(page, 20);

        Page<ReportResponseDTO> reports =
                reportService.reportPage(pageable, reason);

        model.addAttribute("reports", reports);
        model.addAttribute("pageTitle", "신고 목록");
        // model.addAttribute("pageCss", "/css/itemPost/list.css");
        //  model.addAttribute("pageJs", "/js/itemPost/list.js");
        model.addAttribute("body", "Report/list"); // .jsp 제외, prefix로 연결


        return "layout/layout";
    }

    @GetMapping("/detail/{id}")
    public String reportDetail(@PathVariable Long id, Model model) {

        ReportResponseDTO report = reportService.reportDetail(id);

        model.addAttribute("report", report);
        model.addAttribute("pageTitle", "신고 상세");
        model.addAttribute("body", "Report/detail");

        return "layout/layout";
    }
/*
    @PostMapping("/{id}/process")
    public String processReport(@PathVariable Long id) {
        reportService.processReport(id);
        return "redirect:/admin/reports/" + id;
    }

    @PostMapping("/{id}/reject")
    public String rejectReport(@PathVariable Long id) {
        reportService.rejectReport(id);
        return "redirect:/admin/reports/" + id;
    }

 */
}


