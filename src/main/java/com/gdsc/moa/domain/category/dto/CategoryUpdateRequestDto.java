package com.gdsc.moa.domain.category.dto;

import lombok.Getter;

@Getter
public class CategoryUpdateRequestDto {
    private Long originalId;
    private String changeCategoryName;
}
