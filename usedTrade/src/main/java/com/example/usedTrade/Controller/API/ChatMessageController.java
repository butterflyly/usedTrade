package com.example.usedTrade.Controller.API;

import com.example.usedTrade.DTO.Request.Chat.ChatMessageRequestDto;
import com.example.usedTrade.Service.ChatRoom.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/send") // 👈 /pub/chat/send
    public void send(ChatMessageRequestDto dto,
                     Principal principal) {

        chatMessageService.send(dto, principal.getName());
    }

}

