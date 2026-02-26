package com.example.usedTrade.Repository.ItemPost;

import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Item.Enum.ItemStatus;
import com.example.usedTrade.Entity.Users.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ItemPostRepository extends JpaRepository<ItemPost, Long> {


    Page<ItemPost> findAll(Pageable pageable);


    // 판매자 + 상태 기준 조회
    List<ItemPost> findBySellerAndStatus(Users seller, ItemStatus status);


    @Query("select i from ItemPost i" +
            " where i.category.id = :categoryId" +
            " and i.title like  %:kw% and i.status = :status")
    Page<ItemPost> findAllByTitleByKeywordAndTypeAndCategory(@Param("kw") String kw,
                                                  @Param("categoryId") Long categoryId,
                                                  Pageable pageable,@Param("status")ItemStatus itemStatus);

    @Query("select i from ItemPost i " +
            "where i.category.id = :categoryId " +
            "and (i.title like %:kw% or i.content like %:kw% and i.status = :status)")
    Page<ItemPost> findAllTitleOrContentByKeywordAndTypeAndCategory(@Param("kw")String kw,
                                                         @Param("categoryId") Long categoryId,
                                                         Pageable pageable, @Param("status") ItemStatus itemStatus);


    // 닉네임 검색 + 카테고리
    @Query("select i from ItemPost i join i.seller s " +
            "where i.category.id = :categoryId " +
            "and s.nickname like %:kw% and i.status = :status")
    Page<ItemPost> findAllByNicknameByKeywordAndTypeAndCategory(@Param("kw") String kw,
                                                     @Param("categoryId") Long categoryId,Pageable pageable
                                                                , @Param("status") ItemStatus status);

    @Modifying
    // @Modifying은 @Query로 INSERT, UPDATE, DELETE 및 DDL을 직접 작성하여
    // 사용할 때 수정하여 직접 작성한 쿼리라는것을 명시
    @Query("update ItemPost i set i.views = i.views + 1 where i.id = :id")
    @Transactional
    void updateView(Long id);

    Page<ItemPost> findBySellerUsernameAndStatus(String username, ItemStatus itemStatus, Pageable pageable);

    Page<ItemPost> findBySellerUsernameAndStatusIn(String username, List<ItemStatus> onSale, Pageable pageable);
}