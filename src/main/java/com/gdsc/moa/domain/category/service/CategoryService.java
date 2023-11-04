package com.gdsc.moa.domain.category.service;

import com.gdsc.moa.domain.category.dto.CategoryCreateResponseDto;
import com.gdsc.moa.domain.category.dto.CategoryResponseDto;
import com.gdsc.moa.domain.category.entity.CategoryEntity;
import com.gdsc.moa.domain.category.repository.CategoryRepository;
import com.gdsc.moa.domain.user.entity.UserEntity;
import com.gdsc.moa.domain.user.repository.UserRepository;
import com.gdsc.moa.global.exception.ApiException;
import com.gdsc.moa.global.message.GifticonMessage;
import com.gdsc.moa.global.message.UserMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;


    @Transactional
    public CategoryResponseDto createCategory(CategoryCreateResponseDto categoryName, String email) {
        UserEntity user = findUser(email);
        CategoryEntity categoryEntity = categoryRepository.findByCategoryName(categoryName.getCategoryName()).orElse(new CategoryEntity());

        categoryEntity.createCategory(categoryName.getCategoryName(), user);
        categoryEntity = categoryRepository.save(categoryEntity);
        return new CategoryResponseDto(categoryEntity.getId(), categoryEntity.getCategoryName());
    }

    public List<CategoryResponseDto> getCategory(String email) {
        UserEntity user = findUser(email);
        List<CategoryEntity> userCategories = categoryRepository.findAllByUser(user);
        if (userCategories.isEmpty()) {
            throw new ApiException(GifticonMessage.CATEGORY_NOT_FOUND);
        }
        List<CategoryResponseDto> categoryResponseList = new ArrayList<>();
        for (CategoryEntity categoryEntity : userCategories) {
            CategoryResponseDto categoryResponse = new CategoryResponseDto(categoryEntity.getId(), categoryEntity.getCategoryName());
            categoryResponseList.add(categoryResponse);
        }
        return categoryResponseList;
    }


    public UserEntity findUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ApiException(UserMessage.USER_NOT_FOUND));
    }
}
