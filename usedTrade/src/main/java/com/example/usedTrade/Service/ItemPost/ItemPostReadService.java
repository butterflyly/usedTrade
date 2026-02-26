package com.example.usedTrade.Service.ItemPost;

import com.example.usedTrade.DTO.Response.ItemPost.ItemPostResponseDto;
import com.example.usedTrade.DTO.Response.ItemPost.ReceivedReviewDto;
import com.example.usedTrade.Entity.Item.Deal;
import com.example.usedTrade.Entity.Item.Enum.DealStatus;
import com.example.usedTrade.Entity.Item.ItemCategory;
import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Item.Enum.ItemStatus;
import com.example.usedTrade.Entity.Users.MyPageTab;
import com.example.usedTrade.Entity.Users.Users;
import com.example.usedTrade.Repository.ItemPost.*;
import com.example.usedTrade.Repository.Users.UserRepository;
import com.example.usedTrade.Service.Users.UserReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용
@Slf4j
public class ItemPostReadService {

    private final ItemPostRepository itemPostRepository;
    private final UserRepository userRepository;
    private final ItemLikeRepository itemLikeRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final DealRepository dealRepository;

    // 카테고리 조회
    public List<ItemCategory> categoryFindAll()
    {
        return itemCategoryRepository.findAll();
    }


    @PreAuthorize("@itemPostReadService.isOwner(#id, authentication.name)")
    public ItemPostResponseDto getItemPostForUpdate(Long id) {
        return getItemPost(id);
    }

    public boolean isOwner(Long postId, String username) {
        return itemPostRepository.findById(postId)
                .map(post -> {
                    if (post.getSeller() == null) return false; // 작성자가 없으면 수정 불가
                    return post.getSeller().getUsername().equals(username);
                })
                .orElse(false); // 게시글 자체가 없으면 수정 불가
    }


    public Page<ItemPostResponseDto> getAllItemPosts(Pageable pageable,String type,
                                                     String kw,Long categoryId,ItemStatus itemStatus) {

        Page<ItemPost> itemPostPage;

        // 제목
        if(type.equals("title"))
        {
            log.info("제목 검색");
            itemPostPage = itemPostRepository.findAllByTitleByKeywordAndTypeAndCategory(kw,categoryId,pageable,itemStatus);
        }
        // 제목 / 내용
        else if (type.equals("title_Content")) {
            log.info("제목 / 내용 검색");
            itemPostPage = itemPostRepository.findAllTitleOrContentByKeywordAndTypeAndCategory(kw,categoryId, pageable,itemStatus);
        }
        // 닉네임
        else {
            log.info("닉네임 검색");
            itemPostPage = itemPostRepository.findAllByNicknameByKeywordAndTypeAndCategory(kw,categoryId,pageable,itemStatus);
        }

        return itemPostPage.map(ItemPostResponseDto::new);

    }

    public ItemPostResponseDto getItemPost(Long id) {
        ItemPost itemPost = itemPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        log.info("🟢 post 객체: {}", itemPost);
        log.info("🟢 post.id: {}",itemPost.getId());
        log.info("🟢 post hash: {}", System.identityHashCode(itemPost));

        return new ItemPostResponseDto(itemPost);
    }

    // 좋아요 한 게시글 보기
    public List<ItemPostResponseDto> getLikedPosts(String username) {
        Users user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저"));

        List<ItemPost> likedPosts = itemLikeRepository.findLikedPostsByUsers(user);


        return likedPosts.stream().map(ItemPostResponseDto::new).toList();
    }

    public String categoryName(Long categoryId) {

        ItemCategory itemCategory = itemCategoryRepository.findById(categoryId).orElseThrow();

        return itemCategory.getName();
    }

    public List<ItemPostResponseDto> findSellingByUser(Long userId) {

        Users user = userRepository.findById(userId).orElseThrow();

        return itemPostRepository
                .findBySellerAndStatus(user, ItemStatus.ON_SALE)
                .stream()
                .map(ItemPostResponseDto::from)
                .toList();
    }
/*
    public Page<ReceivedReviewDto> getReceivedReviews(String username, Pageable pageable) {
        Page<Deal> deals =
                dealRepository.findBySellerUsernameAndStatusAndReviewIsNotNull(
                        username,
                        DealStatus.COMPLETED,
                        pageable
                );

        return deals.map(ReceivedReviewDto::new);
    }

 */


    public Page<ItemPostResponseDto> getMyPostsByStatus(
            String username,
            MyPageTab tab,
            Pageable pageable
    ) {
        Page<ItemPost> page;

        log.info("pageable = {}", pageable);


        if (tab == MyPageTab.COMPLETED) {
            page = itemPostRepository
                    .findBySellerUsernameAndStatus(
                            username,
                            ItemStatus.SOLD_OUT,
                            pageable
                    );
        } else { // SELLING
            page = itemPostRepository
                    .findBySellerUsernameAndStatusIn(
                            username,
                            List.of(ItemStatus.ON_SALE, ItemStatus.RESERVED),
                            pageable
                    );
        }

        return page.map(ItemPostResponseDto::new);
    }


}
