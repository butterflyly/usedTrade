package com.example.usedTrade.Entity.Users;

import lombok.Getter;

@Getter
public enum UserRole {

    NORMAL("N", "일반 회원"),
    ADMIN("A", "관리자");

    private final String code;
    private final String description;

    UserRole(String code, String description) {
        this.code = code;
        this.description = description;
    }


}
