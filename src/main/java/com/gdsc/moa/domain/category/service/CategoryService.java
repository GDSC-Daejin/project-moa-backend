package com.gdsc.moa.domain.category.service;

import com.gdsc.moa.domain.category.dto.CategoryCreateResponseDto;
import com.gdsc.moa.domain.category.dto.CategoryResponseDto;
import com.gdsc.moa.domain.category.entity.CategoryEntity;
import com.gdsc.moa.domain.category.repository.CategoryRepository;
import com.gdsc.moa.domain.user.entity.UserEntity;
import com.gdsc.moa.domain.user.repository.UserRepository;
import com.gdsc.moa.global.exception.ApiException;
import com.gdsc.moa.global.message.UserMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    

    @Transactional
    public CategoryResponseDto createCategory(CategoryCreateResponseDto categoryName, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(UserMessage.USER_NOT_FOUND));
        CategoryEntity categoryEntity = categoryRepository.findByCategoryName(categoryName.getCategoryName()).orElse(new CategoryEntity());

        categoryEntity.createCategory(categoryName.getCategoryName(), user);
        categoryEntity = categoryRepository.save(categoryEntity);
        return new CategoryResponseDto(categoryEntity.getId(), categoryEntity.getCategoryName());
    }

}
