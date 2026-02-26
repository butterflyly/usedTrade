package com.example.usedTrade.Controller.API.ItemPost;

import com.example.usedTrade.DTO.Request.ItemPost.ItemStatusUpdateRequest;
import com.example.usedTrade.DTO.Response.ItemPost.ItemPostResponseDto;
import com.example.usedTrade.DTO.Response.Users.UserResponseDto;
import com.example.usedTrade.Form.ItemPostWriteForm;
import com.example.usedTrade.Service.ItemPost.ItemPostCommadService;
import com.example.usedTrade.Service.ItemPost.ItemPostReadService;
import com.example.usedTrade.Service.Users.UserReadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item-posts")
@Slf4j
public class ItemPostApiController {

    private final ItemPostCommadService itemPostCommadService;
    private final ItemPostReadService itemPostReadService;
    private final UserReadService userReadService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createItemPost(
               @Valid @ModelAttribute ItemPostWriteForm form,
               BindingResult bindingResult,
               @RequestPart(value = "files", required = false) List<MultipartFile> files,
               @AuthenticationPrincipal UserDetails userDetails) throws IOException {

             log.info("🔥 컨트롤러 진입");
             log.info("dto = {}", form);
             log.info("files = {}", files != null ? files.size() : 0);

            log.info("bindingResult.hasErrors() = {}", bindingResult.hasErrors());
            log.info("errors = {}", bindingResult.getAllErrors());

            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getAllErrors()
                        .stream()
                        .map(ObjectError::getDefaultMessage)
                        .toList();

                return ResponseEntity.badRequest().body(errors);
            }

        // 🔽 이미지 검증
        if (files != null && files.size() > 5) {
            return ResponseEntity.badRequest()
                    .body(List.of("이미지는 최대 5장까지 업로드할 수 있습니다."));
        }

            if (userDetails == null) {
                log.info(" 게시글 작성 API 비로그인 접속");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
            }

            String username = userDetails.getUsername();

            ItemPostResponseDto response = itemPostCommadService.createItemPost(form,username,
                   files);

            return ResponseEntity.ok(response);

    }

    // 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @Valid @ModelAttribute ItemPostWriteForm form,
            BindingResult bindingResult,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "deleteImageIds", required = false)
            List<Long> deleteImageIds)
            throws IOException {

        log.info("🔥 컨트롤러 진입");
        log.info("dto = {}", form);
        log.info("files = {}", files != null ? files.size() : 0);


        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();

            log.info("에러사항 = {}", errors);


            return ResponseEntity.badRequest().body(errors);
        }

        itemPostCommadService.updateItemPost(id, form,files,deleteImageIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL')") // 로그인 여부 확인
    public ResponseEntity<Void> deletePost(@PathVariable Long id,
                                           @AuthenticationPrincipal UserDetails userDetails) {

        log.info("삭제 접근했음?");

        ItemPostResponseDto post = itemPostReadService.getItemPost(id); // 게시글 존재 여부 체크

        log.info("post.getSeller(): {}", post.getSeller());
        log.info("userDetails.getUsername(): {}", userDetails.getUsername());
        log.info("작성자 일치: {}", post.getSeller() != null && post.getSeller().getUsername().equals(userDetails.getUsername()));
        log.info("관리자 여부: {}", userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        // 작성자 혹은 관리자인지 체크
        if (post.getSeller() == null ||
                (!post.getSeller().getUsername().equals(userDetails.getUsername()) &&
                        !userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        itemPostCommadService.DeleteItemPost(id);
        return ResponseEntity.ok().build();
    }

    // 판매 상태 변경
    @PatchMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody ItemStatusUpdateRequest dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        itemPostCommadService.updateStatus(id, dto.getStatus(), userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    // 찜(좋아요) 기능
    // 추천기능
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/vote/{id}")
    public ResponseEntity<?> BoardVote(@AuthenticationPrincipal UserDetails userDetails,
                            @PathVariable("id") Long id) {

        UserResponseDto userResponseDto = userReadService.getUserResponseDTO(userDetails.getUsername());
        ItemPostResponseDto itemPostResponseDto = itemPostReadService.getItemPost(id);

        if(userResponseDto.getId().equals(itemPostResponseDto.getSeller().getId()))
        {
            return ResponseEntity
                    .badRequest()
                    .body("자기 게시글은 찜할 수 없습니다.");
        }

        itemPostCommadService.vote(id,userResponseDto.getUsername());
        return ResponseEntity.ok().build();
    }

}

