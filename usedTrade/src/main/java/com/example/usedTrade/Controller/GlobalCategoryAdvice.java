package com.example.usedTrade.Controller;

import com.example.usedTrade.Entity.Item.ItemCategory;
import com.example.usedTrade.Repository.ItemPost.ItemCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalCategoryAdvice {

    private final ItemCategoryRepository itemCategoryRepository;

    @ModelAttribute("categories")
    public List<ItemCategory> categories() {
        return itemCategoryRepository.findAll();
    }
}