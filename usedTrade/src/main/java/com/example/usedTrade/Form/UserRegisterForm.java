package com.example.usedTrade.Form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterForm {

    private String username;
    private String password;
    private String password2;
    private String nickname;
    private String email;
}