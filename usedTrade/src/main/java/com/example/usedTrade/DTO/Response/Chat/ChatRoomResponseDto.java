package com.example.usedTrade.DTO.Response.Chat;


import com.example.usedTrade.DTO.Response.Users.UserResponseDto;
import com.example.usedTrade.Entity.Chat.ChatRoom;
import com.example.usedTrade.Entity.Users.Users;
import lombok.Getter;

@Getter
public class ChatRoomResponseDto {

    private Long roomId;

    /** 상대방 (무조건 이거만 씀) */
    private UserResponseDto opponent;

    /** 로그인 유저 기준 */
    private boolean seller;  // true = 판매중

    private int unreadCount;

    public ChatRoomResponseDto(
            ChatRoom chatRoom,
            Users me,
            int unreadCount) {

        this.roomId = chatRoom.getId();
        this.unreadCount = unreadCount;

        boolean isSeller =
                chatRoom.getSeller().getId().equals(me.getId());

        this.seller = isSeller;

        Users opponentUser = isSeller
                ? chatRoom.getBuyer()
                : chatRoom.getSeller();

        this.opponent = UserResponseDto.from(opponentUser);
    }
}