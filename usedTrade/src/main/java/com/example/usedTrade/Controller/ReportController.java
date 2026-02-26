package com.example.usedTrade.Controller;

import com.example.usedTrade.DTO.Request.ReportCreateRequest;
import com.example.usedTrade.Service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody @Valid ReportCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        reportService.create(request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}