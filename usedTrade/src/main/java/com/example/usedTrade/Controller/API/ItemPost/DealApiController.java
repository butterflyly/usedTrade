package com.example.usedTrade.Controller.API.ItemPost;

import com.example.usedTrade.Service.ItemPost.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deals")
@RequiredArgsConstructor
public class DealApiController {

    private final DealService dealService;

    // 구매확정
    @PostMapping("/request/{postId}")
    public void requestDeal(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {

        dealService.requestDeal(postId, userDetails.getUsername());
    }

    // 판매확정
    @PostMapping("/confirm/{dealId}")
    public void confirmDeal(
            @PathVariable Long dealId,
            @AuthenticationPrincipal UserDetails userDetails) {

        dealService.confirmDeal(dealId, userDetails.getUsername());
    }

    // 거래취소
    @PostMapping("/cancel/{dealId}")
    public void cancelDeal(
            @PathVariable Long dealId,
            @AuthenticationPrincipal UserDetails userDetails) {

        dealService.cancelDeal(dealId, userDetails.getUsername());
    }
}
