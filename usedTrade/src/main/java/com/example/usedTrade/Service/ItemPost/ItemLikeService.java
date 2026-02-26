package com.example.usedTrade.Service.ItemPost;

import com.example.usedTrade.DTO.Response.ItemPost.ItemLikeResponseDto;
import com.example.usedTrade.Entity.Item.ItemLike;
import com.example.usedTrade.Entity.Users.Users;
import com.example.usedTrade.Repository.ItemPost.ItemLikeRepository;
import com.example.usedTrade.Repository.Users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemLikeService {

    private final ItemLikeRepository itemLikeRepository;
    private final UserRepository userRepository;


    public Page<ItemLikeResponseDto> getLikedPostPage(String username, Pageable likedPageable) {

        Users user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저"));

        Page<ItemLike> likes =
                itemLikeRepository.findByUsersOrderByCreatedAtDesc(user, likedPageable);

        Page<ItemLikeResponseDto> postResponseDtos = likes.map(
                ItemLikeResponseDto::new);

        return postResponseDtos;
    }
}
