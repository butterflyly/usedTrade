package com.example.usedTrade.DTO.Response.Users;

import com.example.usedTrade.Entity.Users.UserImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserImageResponseDto {

    private Long user_Id;
    private String url;

    public UserImageResponseDto(UserImage userImage)
    {
       this.user_Id = userImage.getUsers().getId();
       this.url = userImage.getUrl();

    }
}
