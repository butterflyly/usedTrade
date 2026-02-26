package com.example.usedTrade.DTO.Request.Users;

import com.example.usedTrade.Entity.Users.UserStatus;
import lombok.Getter;

@Getter
public class UserStatusRequestDto {
    private UserStatus status;
}