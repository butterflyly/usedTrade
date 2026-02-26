package com.example.usedTrade.Controller.API;

import com.example.usedTrade.DTO.Response.Chat.ChatMessageResponseDto;
import com.example.usedTrade.Service.ChatRoom.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatMessageApiController {

    private final ChatMessageService chatMessageService;

    @GetMapping("/rooms/{roomId}/messages")
    public List<ChatMessageResponseDto> getMessages(
            @PathVariable Long roomId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return chatMessageService.getMessages(roomId, userDetails.getUsername());
    }
}