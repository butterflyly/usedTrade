package com.example.usedTrade.Controller;
/*
import com.example.usedTrade.DTO.ChartData;
import com.example.usedTrade.DTO.Request.Users.UserStatusRequestDto;
import com.example.usedTrade.DTO.Response.Users.UserResponseDto;
import com.example.usedTrade.Entity.Users.UserStatus;
import com.example.usedTrade.Entity.Users.Users;
import com.example.usedTrade.Service.Users.UserCommandService;
import com.example.usedTrade.Service.Users.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserReadService userReadService;
    private final UserCommandService userCommandService;

    @GetMapping
    public String userPageList(  @RequestParam(required = false) UserStatus status,
                                 @RequestParam(defaultValue = "0") int page,
                                 Model model)
    {
        Page<UserResponseDto> userPageList = userReadService.getUserPageList(status,page);

        model.addAttribute("users", userPageList.getContent()); // 🔥 여기
        model.addAttribute("pageInfo", userPageList);           // 페이지 정보 따로
        model.addAttribute("status", status); // 탭 유지용
        model.addAttribute("pageTitle", "유저 목록");

        model.addAttribute("body", "Admin/list"); // .jsp 제외, prefix로 연결

        return "layout/layout";
    }

    @GetMapping("/{id}")
    public String userDetail(@PathVariable Long id, Model model) {

        UserResponseDto user = userReadService.getUserDetail(id);

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "유저 상세");
        model.addAttribute("body", "Admin/detail"); // .jsp 제외, prefix로 연결

        return "layout/layout";
    }

    @PatchMapping("/{userId}/status")
    @ResponseBody
    public ResponseEntity<Void> changeUserStatus(
            @PathVariable Long userId,
            @RequestBody UserStatusRequestDto dto) {
        userCommandService.changeStatus(userId, dto.getStatus());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/soft/list")
    public String SoftUserList(@RequestParam(defaultValue = "0") int page,
                               Model model)
    {
        Page<UserResponseDto> userPageList = userReadService.getUserSoftPageList(page);
        model.addAttribute("users", userPageList);
        model.addAttribute("pageTitle", "소프트 삭제 유저 목록");
        model.addAttribute("body", "Admin/Soft_list"); // .jsp 제외, prefix로 연결

        return "layout/layout";
    }

    @GetMapping("/soft/{id}")
    public String SoftUserDetail(@PathVariable Long id, Model model)
    {
        UserResponseDto user = userReadService.getDeletedUser(id);

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "탈퇴 유저 상세");
        model.addAttribute("body", "Admin/soft_detail");

        return "layout/layout";
    }

    @PostMapping("/{id}/restore")
    @ResponseBody
    public ResponseEntity<Void> restoreUser(@PathVariable Long id) {

        userCommandService.restoreUser(id);
        return ResponseEntity.ok().build();
    }


 */
    /*
   회원가입 일자 차트
   */
/*
    @GetMapping("/register/chart")
    public String RegisterChart(Model model)
    {
        ChartData chartData =  userReadService.RegisterChart();
        model.addAttribute("chartData", chartData);
        model.addAttribute("pageTitle", "회원가입 통계");
        model.addAttribute("body", "Admin/register_chart");

        return "layout/layout";
    }



}

 */
