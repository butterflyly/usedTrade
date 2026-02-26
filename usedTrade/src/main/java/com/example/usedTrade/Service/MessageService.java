package com.example.usedTrade.Service;

import com.example.usedTrade.Entity.Users.Users;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public void send(Users user, String content) {
        // TODO
        // - 알림 테이블
        // - 쪽지
        // - 이메일
        // 지금은 로그만
        System.out.println("[알림] " + user.getUsername() + " : " + content);
    }
}