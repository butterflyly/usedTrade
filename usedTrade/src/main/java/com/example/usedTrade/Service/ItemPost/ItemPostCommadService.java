package com.example.usedTrade.Service.ItemPost;

import com.example.usedTrade.DTO.Request.ItemPost.ItemPostRequestDto;
import com.example.usedTrade.DTO.Response.ItemPost.ItemPostResponseDto;
import com.example.usedTrade.Entity.Item.*;
import com.example.usedTrade.Entity.Item.Enum.ItemStatus;
import com.example.usedTrade.Entity.Users.Users;
import com.example.usedTrade.Form.ItemPostWriteForm;
import com.example.usedTrade.Repository.ItemPost.ItemCategoryRepository;
import com.example.usedTrade.Repository.ItemPost.ItemLikeRepository;
import com.example.usedTrade.Repository.ItemPost.ItemPostImageRepository;
import com.example.usedTrade.Repository.ItemPost.ItemPostRepository;
import com.example.usedTrade.Repository.Users.UserRepository;
import com.example.usedTrade.Service.FileStorageService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemPostCommadService {

    private final ItemPostRepository itemPostRepository;
    private final UserRepository userRepository;
    private final ItemPostImageRepository itemPostImageRepository;
    private final ItemLikeRepository itemLikeRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final FileStorageService fileStorageService;

    private static final int MAX_IMAGES = 5;

    @Value("${file.boardImagePath}")
    private String uploadFolder;


    private void validateImageCount(
            ItemPost post,
            List<MultipartFile> files,
            List<Long> deleteImageIds
    ) {
        int existing = post.getItemPostImageList().size();
        int delete = deleteImageIds != null ? deleteImageIds.size() : 0;
        int add = files != null ? files.size() : 0;

        if (delete > existing) {
            throw new IllegalArgumentException("삭제 요청 이미지 수가 올바르지 않습니다.");
        }

        if (existing - delete + add > 5) {
            throw new IllegalArgumentException("이미지는 최대 5장까지 업로드할 수 있습니다.");
        }
    }

    private void validateDeleteImages(ItemPost post, List<Long> deleteImageIds) {

        if (deleteImageIds == null || deleteImageIds.isEmpty()) {
            return;
        }

        // 게시글에 실제로 연결된 이미지 ID 집합
        Set<Long> imageIds = post.getItemPostImageList().stream()
                .map(ItemPostImage::getImageId)
                .collect(Collectors.toSet());

        // 삭제 요청 검증
        for (Long deleteId : deleteImageIds) {
            if (!imageIds.contains(deleteId)) {
                throw new IllegalArgumentException("삭제할 수 없는 이미지가 포함되어 있습니다.");
            }
        }

        // 중복 ID 방지 (선택)
        if (imageIds.size() < deleteImageIds.size()) {
            throw new IllegalArgumentException("중복된 삭제 요청이 있습니다.");
        }
    }

    // 파일 생성 메소드
    private void saveImages(ItemPost post, List<MultipartFile> files) {
        if (files == null) return;

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String savedUrl = fileStorageService.save(file);

            ItemPostImage image = ItemPostImage.builder()
                    .url(savedUrl)
                    .itemPost(post)
                    .build();

            itemPostImageRepository.save(image);
        }
    }

    // 파일 삭제 메소드
    private void deleteImages(ItemPost post, List<Long> deleteImageIds) {

        if (deleteImageIds == null || deleteImageIds.isEmpty()) return;

        List<ItemPostImage> images = itemPostImageRepository
                .findByImageIdInAndItemPost(deleteImageIds, post);

        // 1️⃣ 파일 먼저 삭제
        images.forEach(img -> fileStorageService.delete(img.getUrl()));

        // 🔥 엔티티 컬렉션에서도 제거
        post.getItemPostImageList().removeAll(images);

        // 2️⃣ DB 삭제
        itemPostImageRepository.deleteAll(images);
    }

    // 작성
    @Transactional
    public ItemPostResponseDto createItemPost(ItemPostWriteForm itemPostWriteForm, String username,
                                              List<MultipartFile> files) throws IOException {

        if (files != null && files.size() > MAX_IMAGES) {
            throw new IllegalArgumentException(
                    "이미지는 최대 " + MAX_IMAGES + "장까지 업로드할 수 있습니다."
            );
        }

        Users users = userRepository.findByUsernameAndDeletedFalse(username).orElseThrow();

        ItemCategory category = itemCategoryRepository.findById(itemPostWriteForm.getCategoryId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 카테고리"));

        ItemPostRequestDto itemPostRequestDto = new ItemPostRequestDto(
                itemPostWriteForm.getTitle(),
                 itemPostWriteForm.getPrice(),
                itemPostWriteForm.getContent(),
                itemPostWriteForm.getCategoryId());

        ItemPost itemPost = itemPostRequestDto.toEntity(users,category);


        itemPostRepository.save(itemPost);

        saveImages(itemPost, files);

        return itemPost.toResponseDto();
    }

    // 수정
    @Transactional
    public void updateItemPost(Long id, ItemPostWriteForm itemPostWriteForm,List<MultipartFile> files
            ,List<Long> deleteImageIds)
            throws IOException {

        ItemPost post = itemPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수정할 게시물이 존재하지 않습니다."));

        // 🔒 삭제 이미지 유효성
        validateDeleteImages(post, deleteImageIds);

        // 🔢 이미지 개수 검증 (단 한 곳)
        validateImageCount(post, files, deleteImageIds);

        ItemPostRequestDto itemPostRequestDto = new ItemPostRequestDto(
                itemPostWriteForm.getTitle(),
                itemPostWriteForm.getPrice(),
                itemPostWriteForm.getContent(),
                itemPostWriteForm.getCategoryId());

        post.update(itemPostRequestDto); // 엔티티 내부 메서드로 안전하게 변경

        // 1️⃣ 이미지 삭제
        deleteImages(post, deleteImageIds);

        // 2️⃣ 이미지 추가
        saveImages(post, files);



    }

    // 삭제
    @Transactional
    public void DeleteItemPost(Long id)
    {
        // 소프트 삭제는 나중에
        if (itemPostRepository.existsById(id)) {
            itemPostRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("삭제할 게시물이 존재하지 않습니다.");
        }
    }

    // 조회수 로직
    public void updateViews(Long id, HttpServletRequest request,
                            HttpServletResponse response)
    {
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies(); // request 객체의 쿠키들을 가져와 리스트애 담음

        if (cookies != null) {
            for (Cookie cookie : cookies) { // 쿠키 리스트를 찾아본다
                if (cookie.getName().equals("boardViews")) { // 쿠키 리스트 이름중에 "boardViews" 가 있을 경우
                    oldCookie = cookie; // boardViews 의 이름을 담음
                }
            }
        }
        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("["+ id.toString() +"]")) { // id 값이 없는경우(조회를 안한 경우)
                itemPostRepository.updateView(id); // 조회수 증가
              //  ViewsData_ReStore(id,boardResponseDTO.getCategory()); // 조회수 엔티티 저장(Redis로 할수있는지 고민)
                oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]"); // id 값 추가
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24); 							// 쿠키 시간
                response.addCookie(oldCookie); // 쿠키 유지시간 경로를 추가하여 response에 oldCookie 전달
            }
        } else { // oldCookie == null
            itemPostRepository.updateView(id);
          //  ViewsData_ReStore(id,boardResponseDTO.getCategory());
            Cookie newCookie = new Cookie("boardViews", "[" + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24); 								// 쿠키 시간
            response.addCookie(newCookie);
        }

    }

    // 판매상태 변경
    public void updateStatus(Long postId, ItemStatus status, String username) {
        ItemPost post = itemPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        if (!post.getSeller().getUsername().equals(username)) {
            throw new RuntimeException("권한 없음");
        }

        post.setStatus(status);
        itemPostRepository.save(post);
    }

    // 좋아요 기능

    @Transactional
    public void vote(Long itemPostId, String username)
    {
        ItemPost itemPost = itemPostRepository.findById(itemPostId)
                .orElseThrow();

        Users users = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow();

        // 유저가 게시글 내에서 추천을 했는지 찾기
        ItemLike itemLike = itemLikeRepository.findByItemPostAndUsers(itemPost,users);

        // 추천을 한 경우
        if(itemLike != null)
        {
            // 추천 클릭 이벤트므로 추천취소로 해석
            itemPost.LikeMinus(itemLike);

        }
        // 추천을 안한 경우
        else
        {
            ItemLike newLike = ItemLike.builder().
                        users(users)
                        .itemPost(itemPost).createdAt(LocalDateTime.now()).
                        build();

                itemLikeRepository.save(newLike);
                itemPost.addLike(newLike);
            }
        }

}
