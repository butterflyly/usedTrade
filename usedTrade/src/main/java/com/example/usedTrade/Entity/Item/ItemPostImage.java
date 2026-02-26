package com.example.usedTrade.Entity.Item;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ItemPostImage {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long imageId;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "postId")
    private ItemPost itemPost;

}
