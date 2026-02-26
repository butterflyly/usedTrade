package com.example.usedTrade.Entity.Item;

import com.example.usedTrade.Entity.Users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private ItemPost post;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_id", nullable = false, unique = true)
    private Deal deal;

    /** 후기 작성자 (구매자) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Users buyer;

    /** 후기 대상 (판매자) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Users seller;

    /** 평점 (1~5) */
    private int rating;

    /** 후기 내용 */
    @Column(nullable = false, length = 500)
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void update(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }



    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}