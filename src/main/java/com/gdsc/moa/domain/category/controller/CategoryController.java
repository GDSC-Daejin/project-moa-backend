package com.gdsc.moa.domain.category.controller;

import com.gdsc.moa.domain.category.dto.CategoryCreateResponseDto;
import com.gdsc.moa.domain.category.dto.CategoryResponseDto;
import com.gdsc.moa.domain.category.service.CategoryService;
import com.gdsc.moa.global.dto.MoaApiResponse;
import com.gdsc.moa.global.jwt.oauth.UserInfo;
import com.gdsc.moa.global.message.GifticonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("")
    MoaApiResponse<CategoryResponseDto>createCategory(@RequestBody CategoryCreateResponseDto categoryName, @AuthenticationPrincipal UserInfo user){
        CategoryResponseDto response = categoryService.createCategory(categoryName,user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.CATEGORY_CREATE_SUCCESS);
    }

    @GetMapping("")
    MoaApiResponse<List<CategoryResponseDto>>getCategory(@AuthenticationPrincipal UserInfo user){
        List<CategoryResponseDto> response = categoryService.getCategory(user.getEmail());
        return MoaApiResponse.createResponse(response, GifticonMessage.GIFTICON_GET_SUCCESS);
    }

}
