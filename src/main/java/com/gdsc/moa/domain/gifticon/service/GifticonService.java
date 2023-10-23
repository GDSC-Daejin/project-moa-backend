package com.gdsc.moa.domain.gifticon.service;

import com.gdsc.moa.domain.category.entity.CategoryEntity;
import com.gdsc.moa.domain.category.repository.CategoryRepository;
import com.gdsc.moa.domain.gifticon.dto.request.GifticonRequestDto;
import com.gdsc.moa.domain.gifticon.dto.request.GifticonUpdateRequestDto;
import com.gdsc.moa.domain.gifticon.dto.response.GifticonResponseDto;
import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import com.gdsc.moa.domain.gifticon.repository.GifticonRepository;
import com.gdsc.moa.domain.user.entity.UserEntity;
import com.gdsc.moa.domain.user.repository.UserRepository;
import com.gdsc.moa.global.exception.ApiException;
import com.gdsc.moa.global.message.GifticonMessage;
import com.gdsc.moa.global.message.UserMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GifticonService {
    private final UserRepository userRepository;
    private final GifticonRepository gifticonRepository;
    private final CategoryRepository categoryRepository;

    //Gifticon 생성
    @Transactional
    public GifticonResponseDto createGifticon(GifticonRequestDto gifticonRequestDto, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. email=" + email));
        CategoryEntity category = findCategory(gifticonRequestDto.getCategoryId());
        GifticonEntity gifticonEntity = new GifticonEntity(gifticonRequestDto,user,category);
        GifticonEntity savedGifticon = gifticonRepository.save(gifticonEntity);

        return new GifticonResponseDto(savedGifticon);
    }
    @Transactional
    public void deleteGifticon(Long gifticonId, String email) {
        GifticonEntity gifticonEntity = finduserandgifticon(gifticonId, email);
        gifticonRepository.delete(gifticonEntity);
    }
    @Transactional
    public GifticonResponseDto getGifticonDetail(Long gifticonId, String email) {
        GifticonEntity gifticonEntity = finduserandgifticon(gifticonId, email);

        return new GifticonResponseDto(gifticonEntity);
    }
    @Transactional
    public GifticonResponseDto updateGifticon(GifticonUpdateRequestDto gifticonUpdateRequestDto, String email) {
        UserEntity user = findUser(email);
        CategoryEntity category = findCategory(gifticonUpdateRequestDto.getCategoryId());
        GifticonEntity gifticonEntity = findGifticon(gifticonUpdateRequestDto.getId());
        if (!gifticonEntity.getUser().equals(user))
            throw new ApiException(GifticonMessage.GIFTICON_NOT_BELONG_TO_USER);

        GifticonEntity updatedGifticon = new GifticonEntity(gifticonUpdateRequestDto, user,category);

        updatedGifticon = gifticonRepository.save(updatedGifticon);

        return new GifticonResponseDto(updatedGifticon);
    }

    private GifticonEntity finduserandgifticon(Long gifticonId, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(UserMessage.USER_NOT_FOUND));
        return gifticonRepository.findById(gifticonId).orElseThrow(() -> new ApiException(GifticonMessage.GIFTICON_NOT_FOUND) );
    }
    private GifticonEntity findGifticon(Long gifticonId) {
        return gifticonRepository.findById(gifticonId).orElseThrow(() -> new ApiException(GifticonMessage.GIFTICON_NOT_FOUND) );
    }
    private UserEntity findUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ApiException(UserMessage.USER_NOT_FOUND));
    }
    private CategoryEntity findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new ApiException(GifticonMessage.CATEGORY_NOT_FOUND));
    }
}
