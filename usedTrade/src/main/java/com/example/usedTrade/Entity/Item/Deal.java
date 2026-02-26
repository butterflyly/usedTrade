package com.example.usedTrade.Entity.Item;

import com.example.usedTrade.Entity.Item.Enum.DealStatus;
import com.example.usedTrade.Entity.Users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deal {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    private ItemPost itemPost;

    @ManyToOne(fetch = LAZY)
    private Users seller;

    @ManyToOne(fetch = LAZY)
    private Users buyer;

    @Enumerated(EnumType.STRING)
    private DealStatus status;
    // REQUESTED, COMPLETED, CANCELED

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;


    public void changeStatus(DealStatus dealStatus) {
        this.status = dealStatus;
    }
}