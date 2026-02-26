package com.example.usedTrade.Entity.Item;

import com.example.usedTrade.DTO.Request.ItemPost.ItemPostRequestDto;
import com.example.usedTrade.DTO.Response.ItemPost.ItemPostResponseDto;
import com.example.usedTrade.Entity.Item.Enum.ItemStatus;
import com.example.usedTrade.Entity.Users.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "item_post")
public class ItemPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;
    private int price;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int views;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_user_id", nullable = false)
    private Users seller; // 판매자

    @ManyToOne
    private ItemCategory category;

    @Setter // 임시로 붙여줌
    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @Column(name = "like")
    @OneToMany(mappedBy = "itemPost",
            fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ItemLike> itemLikes = new HashSet<>();

    @OneToMany(mappedBy = "itemPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    @Builder.Default
    private List<ItemPostImage> itemPostImageList = new ArrayList<>();


    public ItemPost(String title, int price, String content) {
        this.title = title;
        this.price = price;
        this.content = content;
    }

    // 수정 메서드 (DTO 사용)
    public void update(ItemPostRequestDto dto) {
        this.title = dto.getTitle();
        this.price = dto.getPrice();
        this.content = dto.getContent();
        this.updatedAt = LocalDateTime.now();
    }

    public ItemPostResponseDto toResponseDto() {
        return ItemPostResponseDto.builder()
                .id(id)
                .title(title)
                .price(price)
                .content(content)
                .createdAt(createdAt)
                .createdAtFormatted(createdAt.format(
                        DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 hh시 mm분 a", Locale.KOREA)))
                .build();
    }

    public static ItemPost from(ItemPostRequestDto dto) {
        return new ItemPost(dto.getTitle(), dto.getPrice(), dto.getContent());
    }

    public void LikeMinus(ItemLike itemLike) {
        itemLikes.remove(itemLike);
    }

    public void addLike(ItemLike like) {
        itemLikes.add(like);
        like.setItemPost(this);
    }

    public void changeStatus(ItemStatus itemStatus) {
        this.status = itemStatus;
    }
}