package com.example.usedTrade.Controller.VIEW;

import com.example.usedTrade.Entity.Chat.ChatRoom;
import com.example.usedTrade.Service.ChatRoom.ChatRoomCommandService;
import com.example.usedTrade.Service.ChatRoom.ChatRoomReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomReadService chatRoomReadService;


    @GetMapping("/chat/room/{roomId}")
    public String chatRoomPage(
            @PathVariable Long roomId,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ChatRoom room =
                chatRoomReadService.validateAccess(roomId, userDetails.getUsername());

        model.addAttribute("room", room);
        model.addAttribute("opponent",
                chatRoomReadService.getOpponent(room, userDetails.getUsername()));

        // ⭐ 현재 로그인 유저 정보
        model.addAttribute("myUsername", userDetails.getUsername());

        model.addAttribute("pageTitle", "채팅방");
        model.addAttribute("body", "Chat/room");
        model.addAttribute("pageCss", "/css/Chat/chatRoom.css");
        model.addAttribute("pageJs", "/js/Chat/chatRoom.js");

        return "layout/layout";
    }
}
