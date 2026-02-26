package com.example.usedTrade.Service.ItemPost;

import com.example.usedTrade.DTO.Request.ItemPost.ReviewCreateRequest;
import com.example.usedTrade.Entity.Item.Deal;
import com.example.usedTrade.Entity.Item.Enum.ItemStatus;
import com.example.usedTrade.Entity.Item.Review;
import com.example.usedTrade.Entity.Users.Users;
import com.example.usedTrade.JWT.JwtProvider;
import com.example.usedTrade.Repository.ItemPost.DealRepository;
import com.example.usedTrade.Repository.ItemPost.ReviewRepository;
import com.example.usedTrade.Repository.Users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final DealRepository dealRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public void validateReviewWrite(Long dealId, Users loginUser) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new IllegalArgumentException("거래 정보가 없습니다."));

        if (!deal.getBuyer().getId().equals(loginUser.getId())) {
            throw new AccessDeniedException("구매자만 후기를 작성할 수 있습니다.");
        }

        if (deal.getItemPost().getStatus() != ItemStatus.SOLD_OUT) {
            throw new IllegalStateException("판매 완료된 상품만 후기를 작성할 수 있습니다.");
        }

        if (reviewRepository.existsByDeal(deal)) {
            throw new IllegalStateException("이미 작성된 구매후기가 있습니다.");
        }
    }

    @Transactional
    public void writeReview(ReviewCreateRequest request, Users loginUser) {

        Deal deal = dealRepository.findById(request.getDealId())
                .orElseThrow(() -> new IllegalArgumentException("거래 정보가 없습니다."));

        // 구매자 검증
        if (!deal.getBuyer().getId().equals(loginUser.getId())) {
            throw new AccessDeniedException("구매자만 후기를 작성할 수 있습니다.");
        }

        // 판매 완료 검증
        if (deal.getItemPost().getStatus() != ItemStatus.SOLD_OUT) {
            throw new IllegalStateException("판매 완료된 상품만 후기를 작성할 수 있습니다.");
        }

        // 중복 작성 방지
        if (reviewRepository.existsByDeal(deal)) {
            throw new IllegalStateException("이미 후기가 작성된 거래입니다.");
        }

        Review review = request.toEntity(deal);

        reviewRepository.save(review);
    }

    /**
     * 후기 수정
     */
    public void updateReview(
            Long dealId,
            String content,
            String username
    ) {
        Review review = reviewRepository.findByDealId(dealId)
                .orElseThrow(() -> new IllegalStateException("후기가 존재하지 않습니다."));

        // 구매자 검증
        if (!review.getBuyer().getUsername().equals(username)) {
            throw new AccessDeniedException("후기 수정 권한이 없습니다.");
        }

        review.update(content);
    }

    /**
     * 후기 삭제
     */
    public void deleteReview(
            Long dealId,
            String username
    ) {
        Review review = reviewRepository.findByDealId(dealId)
                .orElseThrow(() -> new IllegalStateException("후기가 존재하지 않습니다."));

        // 구매자 검증
        if (!review.getBuyer().getUsername().equals(username)) {
            throw new AccessDeniedException("후기 삭제 권한이 없습니다.");
        }

        reviewRepository.delete(review);
    }
}
