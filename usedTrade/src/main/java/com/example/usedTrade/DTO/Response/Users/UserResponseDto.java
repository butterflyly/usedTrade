package com.example.usedTrade.DTO.Response.Users;

import com.example.usedTrade.Entity.Users.UserRole;
import com.example.usedTrade.Entity.Users.UserStatus;
import com.example.usedTrade.Entity.Users.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String username;
    private String email;
    private UserRole userRole;
    private String nickname;
    private String displayNickname;
    private UserStatus userStatus;
    private LocalDateTime createdAt;

    public UserResponseDto(Users users)
    {
        this.id = users.getId();
        this.username = users.getUsername();
        this.email = users.getEmail();
        this.userRole =users.getUserRole();
        this.nickname = users.getNickname();
        this.displayNickname = users.getDisplayNickname();
        this.userStatus =users.getStatus();
        this.createdAt = users.getCreatedAt();
    }

    // ✅ 정적 팩토리 메서드 (권장)
    public static UserResponseDto from(Users users) {
        if (users == null) {
            return null;
        }
        return new UserResponseDto(users);
    }
}