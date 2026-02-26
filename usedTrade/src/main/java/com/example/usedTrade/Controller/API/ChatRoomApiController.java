package com.example.usedTrade.Controller.API;


import com.example.usedTrade.DTO.Response.Chat.ChatRoomResponseDto;
import com.example.usedTrade.Service.ChatRoom.ChatRoomCommandService;
import com.example.usedTrade.Service.ChatRoom.ChatRoomReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Slf4j
public class ChatRoomApiController {

    private final ChatRoomCommandService chatRoomService;
    private final ChatRoomReadService chatRoomReadService;


    @PostMapping("/room")
    public ResponseEntity<?> createChatRoom(
            @RequestParam Long itemPostId,
            @AuthenticationPrincipal UserDetails userDetails
    )
    {
        try {
            ChatRoomResponseDto room =
                    chatRoomService.createOrGet(itemPostId, userDetails.getUsername());
            return ResponseEntity.ok(room);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/unread-count")
    public int unreadChatRoomCount(
            @AuthenticationPrincipal UserDetails userDetails) {

        return chatRoomReadService
                .getUnreadChatRoomCount(userDetails.getUsername());
    }

    @PostMapping("/rooms/{roomId}/leave")
    public ResponseEntity<Void> leaveRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        chatRoomService.leaveRoom(roomId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

}
