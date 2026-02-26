package com.example.usedTrade.Repository.ItemPost;

import com.example.usedTrade.Entity.Item.ItemLike;
import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Users.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemLikeRepository extends JpaRepository<ItemLike ,Long> {

    ItemLike findByItemPostAndUsers(ItemPost itemPost, Users users);

    @Query("select l.itemPost from ItemLike l where l.users = :users")
    List<ItemPost> findLikedPostsByUsers(@Param("users") Users users);

    Page<ItemLike> findByUsersOrderByCreatedAtDesc(Users user, Pageable pageable);

}
