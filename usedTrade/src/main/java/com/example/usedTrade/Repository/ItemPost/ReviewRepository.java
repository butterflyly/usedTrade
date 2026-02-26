package com.example.usedTrade.Repository.ItemPost;

import com.example.usedTrade.Entity.Item.Deal;
import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Item.Review;
import com.example.usedTrade.Entity.Users.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    // 판매자가 받은 후기
    Page<Review> findBySeller(Users seller, Pageable pageable);

    // 특정 게시글 후기 존재 여부
    boolean existsByPostAndBuyer(ItemPost post, Users buyer);

    boolean existsByDeal(Deal deal);

    Optional<Review> findByDealId(Long dealId);

    boolean existsByDealId(Long dealId);
}
