package com.example.usedTrade.Entity.Users;

import com.example.usedTrade.Entity.Chat.ChatMessage;
import com.example.usedTrade.Entity.Item.ItemPost;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String nickname;

    @Column(name = "display_nickname")
    private String displayNickname;

    @Column(unique = true)
    private String email;

    private String password;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<ChatMessage> sentMessages;

    // 누적 신고(확정) 횟수
    @Column(nullable = false)
    @Builder.Default
    private int reportCount = 0;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private UserStatus status;


    @OneToMany(mappedBy = "seller", cascade = CascadeType.PERSIST)
    private List<ItemPost> itemPostList;

    private LocalDateTime createdAt;
    private LocalDateTime modifyDate;
    private LocalDateTime deleteDate;

    @Column(name = "provider")
    // provider : google, kakao, naver 이 들어감
    private String providers;

    @Column(name = "providerId")
    // providerId : 구굴 로그인 한 유저의 고유 ID가 들어감
    private String providerIds;

    @Column(nullable = false, name = "is_deleted")
    @Builder.Default
    private boolean deleted = false; // 디폴트 false


    /* ==============================
       생성 / 삭제
    ============================== */

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = UserStatus.USER;
        }
        this.reportCount = 0;
    }

    public void softDelete() {
        this.deleted = true;
        this.deleteDate = LocalDateTime.now();
        this.displayNickname = "탈퇴한 사용자";
    }

    public void restore() {
        this.deleted = false;
        this.displayNickname = this.nickname;
        this.deleteDate = null;

    }


    public void releaseBlack() {
        this.status = UserStatus.USER;
    }

    /* ==============================
       정보 수정
    ============================== */

    public void updateNickname(String nickname) {
        this.nickname = nickname;
        this.modifyDate = LocalDateTime.now();
    }

    public void updatePassword(String password) {
        this.password = password;
        this.modifyDate = LocalDateTime.now();
    }

    /* ==============================
       🚨 신고 / 제재 도메인 로직
    ============================== */

    public void increaseReportCount() {
        this.reportCount++;
    }

    public boolean shouldBeBlack() {
        return this.reportCount >= 3;
    }

    public void warn() {
        this.status = UserStatus.WARNED;
    }

    public void black() {
        this.status = UserStatus.BLACK;
    }

    public void changeStatus(UserStatus status) {
        this.status = status;
    }
}
