package com.example.usedTrade.Repository.ItemPost;

import com.example.usedTrade.Entity.Item.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
}