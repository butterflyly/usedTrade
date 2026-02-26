package com.example.usedTrade.DTO.Request.Users;


import com.example.usedTrade.Entity.Users.UserRole;
import com.example.usedTrade.Entity.Users.Users;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserRequestDto {

    private String username;
    private String nickname;
    private String email;
    private UserRole userRole;


    public Users toEntity(String encodedPassword) {
        return Users.builder()
                .username(username)
                .nickname(nickname)
                .displayNickname(nickname)
                .createdAt(LocalDateTime.now())
                .userRole(userRole)
                .email(email)
                .password(encodedPassword)
                .build();
    }
}