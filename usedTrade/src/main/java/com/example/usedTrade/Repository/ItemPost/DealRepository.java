package com.example.usedTrade.Repository.ItemPost;

import com.example.usedTrade.Entity.Item.Deal;
import com.example.usedTrade.Entity.Item.Enum.DealStatus;
import com.example.usedTrade.Entity.Item.Enum.ItemStatus;
import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Users.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DealRepository extends JpaRepository<Deal,Long> {

    Optional<Deal>
    findByItemPostAndBuyerOrSeller(ItemPost itemPost, Users buyer, Users seller);


    // 구매 내역
    Page<Deal> findByBuyerUsernameAndItemPost_Status(
            String username,
            ItemStatus status,
            Pageable pageable
    );

    // 받은 후기
    @Query("""
    select d, r
    from Deal d
    join Review r on r.deal = d
    where d.seller.username = :username
    """)
    Page<Object[]> findReceivedReviews(
            @Param("username") String username,
            Pageable pageable
    );

}
