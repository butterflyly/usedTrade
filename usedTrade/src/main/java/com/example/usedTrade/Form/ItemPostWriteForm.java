package com.example.usedTrade.Form;

import com.example.usedTrade.Entity.Item.ItemCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPostWriteForm {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @NotNull(message = "가격을 입력해주세요.")   // ⭐ 이거 중요
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private Integer price;

    @NotNull(message = "카테고리를 선택해주세요.")
    @Min(value = 1, message = "카테고리를 선택해주세요.")
    private Long categoryId;
}
