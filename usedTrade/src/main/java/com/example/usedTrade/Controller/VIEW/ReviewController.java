package com.example.usedTrade.Controller.VIEW;

import com.example.usedTrade.DTO.Request.ItemPost.ReviewCreateRequest;
import com.example.usedTrade.Entity.Users.Users;
import com.example.usedTrade.Service.ItemPost.ReviewService;
import com.example.usedTrade.Service.Users.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserReadService userService;

    @PostMapping("/reviews")
    public String writeReview(@ModelAttribute ReviewCreateRequest request,
                              @AuthenticationPrincipal UserDetails userDetails) {

        Users loginUser = userService.findByUsername(userDetails.getUsername());
        reviewService.writeReview(request, loginUser);

        return "redirect:/item-posts/detail/" + request.getDealId();
    }
}
