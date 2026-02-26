package com.example.usedTrade.Controller.API.ItemPost;

import com.example.usedTrade.DTO.Request.ItemPost.ReviewRequestDto;
import com.example.usedTrade.Service.ItemPost.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewApiController {

    private final ReviewService reviewService;

    @PutMapping("/{dealId}")
    public ResponseEntity<?> update(
            @PathVariable Long dealId,
            @RequestBody ReviewRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        reviewService.updateReview(
                dealId,
                dto.getContent(),
                userDetails.getUsername()
        );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{dealId}")
    public ResponseEntity<?> delete(
            @PathVariable Long dealId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        reviewService.deleteReview(
                dealId,
                userDetails.getUsername()
        );
        return ResponseEntity.ok().build();
    }
}
