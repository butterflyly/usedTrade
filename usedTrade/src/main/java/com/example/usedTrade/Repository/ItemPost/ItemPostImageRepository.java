package com.example.usedTrade.Repository.ItemPost;

import com.example.usedTrade.Entity.Item.ItemPost;
import com.example.usedTrade.Entity.Item.ItemPostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemPostImageRepository extends JpaRepository<ItemPostImage ,Long> {
    List<ItemPostImage> findByImageIdInAndItemPost(
            List<Long> deleteImageIds,
            ItemPost post
    );

    int countByItemPost_Id(Long id);
}
