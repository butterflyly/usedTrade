package com.example.usedTrade.Entity.Users;

public enum UserStatus {

    USER("U", "정상"),
    WARNED("W", "경고"),     // 경고
    BLACK("B", "블랙 회원");

    private final String code;
    private final String description;

    UserStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
}