package com.example.usedTrade.Form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateForm {

    @NotBlank(message = "닉네임은 필수입니다.")
    @Pattern(regexp = "^[가-힣A-Za-z0-9]{4,12}$",
            message = "닉네임은 한글(완성형)," +
                    " 영문, 숫자만 사용하여 4~12자로 입력해주세요. (ㄱ,ㄴ,ㅁ 같은 단일 자모는 불가)")
    private String nickname;
}
