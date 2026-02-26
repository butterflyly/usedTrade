package com.example.usedTrade.Entity.Users;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "userimage")
/*
 유저 이미지 엔티티
 */
public class UserImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "User_ID")
    private Users users;


    public void updateUrl(String url) {
        this.url = url;
    }

    private Long delete_user_id;

    public void UserDelete(Long id)
    {
        delete_user_id = id;
        users = null;
    }
}