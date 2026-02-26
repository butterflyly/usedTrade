package com.example.usedTrade.Controller.VIEW;

import com.example.usedTrade.DTO.Response.Chat.ChatRoomResponseDto;
import com.example.usedTrade.DTO.Response.ItemPost.*;
import com.example.usedTrade.DTO.Response.Users.UserImageResponseDto;
import com.example.usedTrade.DTO.Response.Users.UserResponseDto;
import com.example.usedTrade.Form.*;
import com.example.usedTrade.Service.ChatRoom.ChatRoomReadService;
import com.example.usedTrade.Service.ItemPost.DealService;
import com.example.usedTrade.Service.ItemPost.ItemLikeService;
import com.example.usedTrade.Service.ItemPost.ItemPostReadService;
import com.example.usedTrade.Service.Users.UserReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;


import static com.example.usedTrade.Entity.Users.MyPageTab.COMPLETED;
import static com.example.usedTrade.Entity.Users.MyPageTab.SELLING;


@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserViewController {

    private final UserReadService userReadService;
    private final ItemPostReadService itemPostReadService;
    private final ItemLikeService itemLikeService;
    private final ChatRoomReadService chatRoomReadService;
    private final DealService dealService;

    // 회원가입
    @GetMapping("/register")
    public String register(Model model, @AuthenticationPrincipal UserDetails userDetails)
    {
        if(userDetails != null)
        {
            return "redirect:/item-posts";
        }

        model.addAttribute("registerForm" , new UserRegisterForm());

        model.addAttribute("pageTitle", "회원가입");
        model.addAttribute("body", "Users/register");
        model.addAttribute("pageCss", "/css/Users/register.css");
        model.addAttribute("pageJs", "/js/Users/register.js");

        return "layout/layout";
    }

    // 로그인 화면
    @GetMapping("/login")
    public String login(Model model, @AuthenticationPrincipal UserDetails userDetails)
    {
        if(userDetails != null)
        {
            return "redirect:/item-posts";
        }

        model.addAttribute("pageCss", "/css/Users/login.css");
        model.addAttribute("pageJs", "/js/Users/login.js");
        model.addAttribute("pageTitle", "로그인");
        model.addAttribute("loginForm", new LoginForm());
        model.addAttribute("body", "Users/login"); // layout에서 본문 include
        return "layout/layout";

    }


    // 마이페이지
    @GetMapping("/mypage")
    public String MyPage(@AuthenticationPrincipal UserDetails userDetails,
                         @RequestParam(defaultValue = "0") int likedPage,
                         @RequestParam(defaultValue = "0") int sellingPage,

                         @RequestParam(defaultValue = "0") int buyingPage,
                         @RequestParam(defaultValue = "0") int reviewPage,
                         Model model)
    {
        UserResponseDto users = userReadService.getUserResponseDTO(userDetails.getUsername());
        UserImageResponseDto userImageResponseDto = userReadService.getUserImage(users.getId());

        Pageable likedPageable = PageRequest.of(likedPage, 20);
        Pageable sellingPageable = PageRequest.of(sellingPage, 20);
        Pageable buyingPageable = PageRequest.of(buyingPage, 20);
        Pageable reviewPageable = PageRequest.of(reviewPage, 20);

        log.info("sellingPage = {}", sellingPage);
        log.info("sellingPageable = {}", sellingPageable);

        Page<ItemLikeResponseDto> itemLikeResponseDtos = itemLikeService.getLikedPostPage(userDetails.getUsername(), likedPageable);
        Page<ItemPostResponseDto> itemPostResponseDtos = itemPostReadService.getMyPostsByStatus(userDetails.getUsername(), SELLING, sellingPageable);

        Page<DealBuyResponseDto> buyingDeals =
                dealService.getMyBuyingDeals(
                        userDetails.getUsername(),
                        buyingPageable
                );

        Page<DealReviewResponseDto> receivedReviews =
                dealService.getMyReceivedReviews(
                        userDetails.getUsername(),
                        reviewPageable
                );

        model.addAttribute("likedPosts", itemLikeResponseDtos);

        model.addAttribute("sellingPosts", itemPostResponseDtos);

        model.addAttribute("buyingDeals", buyingDeals);
        model.addAttribute("receivedReviews", receivedReviews);


        if(userImageResponseDto == null)
        {
            log.info("UserImageResponseDto: image is NULL");
        }

        log.info("UserImageResponseDto: userId={}, url={}",
                userImageResponseDto.getUser_Id(), userImageResponseDto.getUrl());
        model.addAttribute("user", users);
        model.addAttribute("image",userImageResponseDto);

        model.addAttribute("pageCss", "/css/Users/mypage.css");
        model.addAttribute("pageJs", "/js/Users/mypage.js");

        model.addAttribute("pageTitle", "내 정보");
        model.addAttribute("body", "Users/mypage");

        return "layout/layout";
    }

    // 마이페이지 -> 채팅방 목록이동
    /*
     *** 추후 REST API로 빼기  ***
     */
    @GetMapping("/mypage/chats")
    public String myChatRooms(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable,
            Model model) {

        Page<ChatRoomResponseDto> chatRooms =
                chatRoomReadService.getMyChatRooms(userDetails.getUsername(), pageable);

        model.addAttribute("chatRooms", chatRooms);

        model.addAttribute("pageCss", "/css/Users/mychatList.css");
        model.addAttribute("pageJs", "/js/Users/mychatList.js");

        model.addAttribute("pageTitle", "내 정보");
        model.addAttribute("body", "Users/mychatList");

        return "layout/layout";    }

    // 수정화면
    @GetMapping("/info")
    public String user_update(@AuthenticationPrincipal UserDetails userDetails,Model model)
    {
        UserUpdateForm userUpdateForm = new UserUpdateForm();

        UserResponseDto userResponseDto = userReadService.getUserResponseDTO(userDetails.getUsername());
        UserImageResponseDto userImageResponseDto = userReadService.getUserImage(userResponseDto.getId());

        userUpdateForm.setNickname(userResponseDto.getNickname());

        model.addAttribute("userUpdateForm" , userUpdateForm);

        model.addAttribute("pageCss", "/css/Users/info.css");
        model.addAttribute("pageJs", "/js/Users/info.js");
        model.addAttribute("pageTitle", "회원정보 수정");
        model.addAttribute("body", "Users/update");
        model.addAttribute("image",userImageResponseDto);

        return "layout/layout";

    }


    // 비밀번호 수정
     /*
     비밀번호 수정
     */
    @GetMapping("/pwchange")
    public String PWChange(@ModelAttribute("pwChangeForm") PWChangeForm pwChangeForm,Model model)
    {

        model.addAttribute("pageTitle", "비밀번호 수정");
        model.addAttribute("body", "Users/passwordUpdate");
        model.addAttribute("pageCss", "/css/Users/passwordUpdate.css");
        model.addAttribute("pageJs", "/js/Users/passwordUpdate.js");

        return "layout/layout";
    }


}
