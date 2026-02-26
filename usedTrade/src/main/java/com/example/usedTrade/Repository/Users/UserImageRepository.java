package com.example.usedTrade.Repository.Users;

import com.example.usedTrade.Entity.Users.UserImage;
import com.example.usedTrade.Entity.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage ,Long> {

    UserImage findByUsers(Users users);
}
