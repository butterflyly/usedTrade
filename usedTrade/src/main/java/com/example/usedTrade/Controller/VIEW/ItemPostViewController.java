package com.example.usedTrade.Controller.VIEW;

import com.example.usedTrade.DTO.Request.ItemPost.ItemPostRequestDto;
import com.example.usedTrade.DTO.Response.ItemPost.ItemPostResponseDto;
import com.example.usedTrade.Entity.Item.Deal;
import com.example.usedTrade.Entity.Item.Enum.ItemStatus;
import com.example.usedTrade.Entity.Item.ItemCategory;
import com.example.usedTrade.Form.ItemPostWriteForm;
import com.example.usedTrade.Repository.ItemPost.DealRepository;
import com.example.usedTrade.Service.ItemPost.DealService;
import com.example.usedTrade.Service.ItemPost.ItemPostCommadService;
import com.example.usedTrade.Service.ItemPost.ItemPostReadService;
import com.example.usedTrade.Service.Users.UserReadService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/item-posts")
@Slf4j
public class ItemPostViewController {

    private final ItemPostReadService itemPostService;
    private final ItemPostCommadService itemPostCommadService;
    private final UserReadService userReadService;
    private final DealService dealService;


    // 작성
    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL')")
    public String form(
            @RequestParam(required = false) Long category,
            ItemPostWriteForm itemPostWriteForm,Model model) {

        List<ItemCategory> categories = itemPostService.categoryFindAll(); // DB 조회
        model.addAttribute("categories", categories);

        // 기본 카테고리 세팅
        if (category != null) {
            itemPostWriteForm.setCategoryId(category);
        }

        model.addAttribute("pageCss", "/css/itemPost/write.css");
        model.addAttribute("pageJs", "/js/itemPost/write.js");
        model.addAttribute("itemPostWriteForm", itemPostWriteForm);
        model.addAttribute("pageTitle", "상품 등록");
        model.addAttribute("body", "itemposts/form"); // .jsp 제외, prefix로 연결

        return "layout/layout";
    }

    // 목록 조회
    @GetMapping("/{categoryId}")
    public String list(@PathVariable Long categoryId,
            Model model, @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "createDate") String sort,
                       @RequestParam(defaultValue = "ON_SALE") ItemStatus status,
                       @RequestParam(value = "keyword", defaultValue = "") String kw // 검색
            , @RequestParam(value = "type", defaultValue = "title") String type) // 검색 타입
    {
        String categoryName = itemPostService.categoryName(categoryId);


        Sort sortType;

        switch (sort) {
            case "price":
                sortType = Sort.by(
                        Sort.Order.asc("price"),
                        Sort.Order.desc("createdAt"),
                        Sort.Order.desc("id")
                );
                break;

            case "views":
                sortType = Sort.by(
                        Sort.Order.desc("views"),
                        Sort.Order.desc("createdAt"),
                        Sort.Order.desc("id")
                );
                break;

            default:
                sortType = Sort.by(
                        Sort.Order.desc("createdAt"),
                        Sort.Order.desc("id")
                );
        }

        Pageable pageable = PageRequest.of(page, 20, sortType);

        model.addAttribute("type", type);   // 선택 상태 유지
        model.addAttribute("keyword", kw); // 검색어 유지 가능
        model.addAttribute("pageTitle", "게시글 목록");
        model.addAttribute("pageCss", "/css/itemPost/list.css");
        model.addAttribute("pageJs", "/js/itemPost/list.js");

        model.addAttribute("body", "itemposts/list"); // .jsp 제외, prefix로 연결
        model.addAttribute("categoryName",categoryName);
        model.addAttribute("categoryId",categoryId);

        Page<ItemPostResponseDto> posts = itemPostService.getAllItemPosts(pageable,type,kw,categoryId,status);

        model.addAttribute("posts", posts);
        return "layout/layout"; // prefix + viewName + suffix
    }

    // 상세 조회
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, HttpServletRequest request,
                         HttpServletResponse response,@AuthenticationPrincipal UserDetails userDetails) {

        itemPostCommadService.updateViews(id,request,response); // 조회수 로직 넣기

        Long loginId = 0L;
        Deal myDeal = null;


        if (userDetails != null) {
            loginId = userReadService
                    .getUserResponseDTO(userDetails.getUsername())
                    .getId();

            myDeal = dealService.myDeal(id, userDetails.getUsername());
        }

        ItemPostResponseDto itemPostResponseDto = itemPostService.getItemPost(id);


        // ⭐ Deal 정보 주입
        itemPostResponseDto.setDeal(myDeal);

        model.addAttribute("dealId", myDeal != null ? myDeal.getId() : null);

        model.addAttribute("loginUserId",loginId);
        model.addAttribute("pageCss", "/css/itemPost/detail.css");
        model.addAttribute("pageJs", "/js/itemPost/detail.js");


        boolean isLikedByCurrentUser = itemPostResponseDto.getItemLikesUserId().contains(loginId);

        for(String url: itemPostResponseDto.getImageUrls())
        {
            log.info("아이템 이미지 URL ={}",url);
        }


        model.addAttribute("isLikedByCurrentUser",isLikedByCurrentUser);
        model.addAttribute("post", itemPostResponseDto);
        model.addAttribute("pageTitle", itemPostResponseDto.getTitle());
        model.addAttribute("body", "itemposts/detail");
        return "layout/layout";
    }


    @GetMapping("/modify/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL')")
    public String updateForm(@PathVariable Long id,
                            @ModelAttribute("itemPostWriteForm")
                                 ItemPostWriteForm itemPostWriteForm, Model model)
    {
        ItemPostResponseDto dto = itemPostService.getItemPostForUpdate(id);

        itemPostWriteForm.setTitle(dto.getTitle());
        itemPostWriteForm.setContent(dto.getContent());
        itemPostWriteForm.setPrice(dto.getPrice());
        itemPostWriteForm.setCategoryId(dto.getCategoryId());

        List<ItemCategory> categories = itemPostService.categoryFindAll(); // DB 조회

        model.addAttribute("categories", categories);

        model.addAttribute("id",id);

        model.addAttribute("pageCss", "/css/itemPost/update.css");
        model.addAttribute("pageJs", "/js/itemPost/update.js");
        model.addAttribute("itemPostWriteForm", itemPostWriteForm);
        model.addAttribute("imageList", dto.getImage()); // List<ItemPostImageDto>
        model.addAttribute("pageTitle", "상품 수정");
        model.addAttribute("body", "itemposts/modify"); // .jsp 제외, prefix로 연결

        return "layout/layout";

    }


    // 게시글 수정 검증 로직
    @PostMapping("/update/validate/{id}")
    public String UpdatevalidateForm(@PathVariable Long id,
            @Valid @ModelAttribute("itemPostWriteForm") ItemPostWriteForm form,
            BindingResult bindingResult,
            Model model) {

        model.addAttribute("itemPostWriteForm", form);
        model.addAttribute("id",id);
        model.addAttribute("pageTitle", "상품 수정");

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getFieldErrors());
            model.addAttribute("body", "itemposts/form"); // layout에서 본문 include
            return "layout/layout"; // layout 사용
        }

        ItemPostRequestDto dto = new ItemPostRequestDto(form.getTitle(), form.getPrice(), form.getContent(),form.getCategoryId());
        model.addAttribute("dto", dto);
        model.addAttribute("body", "itemposts/form");
        return "layout/layout";
    }

}