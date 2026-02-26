package com.example.usedTrade.Service.ItemPost;

import com.example.usedTrade.DTO.Response.ItemPost.DealBuyResponseDto;
import com.example.usedTrade.DTO.Response.ItemPost.DealReviewResponseDto;
import com.example.usedTrade.Entity.Alarm.NotificationType;
import com.example.usedTrade.Entity.Item.Deal;
import com.example.usedTrade.Entity.Item.Enum.DealStatus;
import com.example.usedTrade.Entity.Item.Enum.ItemStatus;
import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Item.Review;
import com.example.usedTrade.Entity.Users.Users;
import com.example.usedTrade.Repository.ItemPost.DealRepository;
import com.example.usedTrade.Repository.ItemPost.ItemPostRepository;
import com.example.usedTrade.Repository.Users.UserRepository;
import com.example.usedTrade.Service.Alram.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DealService {

    private final ItemPostRepository itemPostRepository;
    private final UserRepository userRepository;
    private final DealRepository dealRepository;
    private final NotificationService notificationService;


    // 구매 확정
    @Transactional
    public void requestDeal(Long postId, String buyerUsername) {

        ItemPost post = itemPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        Users buyer = userRepository.findByUsernameAndDeletedFalse(buyerUsername)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        if (post.getStatus() != ItemStatus.ON_SALE) {
            throw new IllegalStateException("구매할 수 없는 상태입니다.");
        }

        if (post.getSeller().getId().equals(buyer.getId())) {
            throw new IllegalStateException("본인 상품은 구매할 수 없습니다.");
        }

        Deal deal = Deal.builder()
                .itemPost(post)
                .seller(post.getSeller())
                .buyer(buyer)
                .status(DealStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();

        dealRepository.save(deal);

        notificationService.notify(
                deal.getItemPost().getSeller(),
                NotificationType.DEAL_CONFIRMED,
                "구매자가 구매를 확정했습니다.",
                "/mypage/sales"
        );

        post.changeStatus(ItemStatus.RESERVED);
    }

    // 판매확정
    @Transactional
    public void confirmDeal(Long dealId, String sellerUsername) {

        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new IllegalArgumentException("거래 없음"));

        if (!deal.getSeller().getUsername().equals(sellerUsername)) {
            throw new SecurityException("권한 없음");
        }

        deal.changeStatus(DealStatus.COMPLETED);
        deal.getItemPost().changeStatus(ItemStatus.SOLD_OUT);
    }

    // 거래취소 (구매자 or 판매자)
    @Transactional
    public void cancelDeal(Long dealId, String username) {

        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new IllegalArgumentException("거래 없음"));

        boolean isSeller = deal.getSeller().getUsername().equals(username);
        boolean isBuyer  = deal.getBuyer().getUsername().equals(username);

        if (!isSeller && !isBuyer) {
            throw new SecurityException("권한 없음");
        }

        deal.changeStatus(DealStatus.CANCELED);
        deal.getItemPost().changeStatus(ItemStatus.ON_SALE);
    }

    public Deal myDeal(Long id, String username) {

        ItemPost post = itemPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        Users me = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        return dealRepository
                .findByItemPostAndBuyerOrSeller(post, me, me)
                .orElse(null);
    }

    /**
     * 구매 내역
     * - 내가 구매자
     * - 거래 확정(SOLD_OUT)
     */

    public Page<DealBuyResponseDto> getMyBuyingDeals(
            String username,
            Pageable pageable
    ) {

        Page<Deal> page = dealRepository
                .findByBuyerUsernameAndItemPost_Status(
                        username,
                        ItemStatus.SOLD_OUT,
                        pageable
                );

        return page.map(DealBuyResponseDto::new);
    }

    /**
     *
     * 받은 후기
     * - 내가 판매자
     * - 리뷰 존재
     */
    public Page<DealReviewResponseDto> getMyReceivedReviews(
            String username,
            Pageable pageable
    ) {

        Page<Object[]> page =
                dealRepository.findReceivedReviews(username, pageable);

        return page.map(arr -> {
            Deal deal = (Deal) arr[0];
            Review review = (Review) arr[1];
            return new DealReviewResponseDto(deal, review);
        });
    }
}
