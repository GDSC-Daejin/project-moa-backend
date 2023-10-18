package com.gdsc.moa.domain.gifticon.service;

import com.gdsc.moa.domain.gifticon.dto.request.GifticonRequestDto;
import com.gdsc.moa.domain.gifticon.dto.response.GifticonResponseDto;
import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import com.gdsc.moa.domain.gifticon.entity.Status;
import com.gdsc.moa.domain.gifticon.repository.GifticonRepository;
import com.gdsc.moa.domain.user.entity.UserEntity;
import com.gdsc.moa.domain.user.repository.UserRepository;
import com.gdsc.moa.global.exception.ApiException;
import com.gdsc.moa.global.message.GifticonMessage;
import com.gdsc.moa.global.message.UserMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GifticonService {
    private final UserRepository userRepository;
    private final GifticonRepository gifticonRepository;

    //Gifticon 생성
    public GifticonResponseDto createGifticon(GifticonRequestDto gifticonRequestDto, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. email=" + email));
        GifticonEntity gifticonEntity = GifticonEntity.builder()
                .name(gifticonRequestDto.getName())
                .barcodeNumber(gifticonRequestDto.getBarcodeNumber())
                .gifticonImagePath(gifticonRequestDto.getGifticonImagePath())
                .exchangePlace(gifticonRequestDto.getExchangePlace())
                .dueDate(gifticonRequestDto.getDueDate())
                .gifticonType(gifticonRequestDto.getGifticonType())
                .orderNumber(gifticonRequestDto.getOrderNumber())
                .status(Status.AVAILABLE)
                .usedDate(null)
                .user(user)
                .build();
        GifticonEntity savedGifticon = gifticonRepository.save(gifticonEntity);

        return new GifticonResponseDto(savedGifticon);
    }

    public GifticonResponseDto deleteGifticon(Long gifticonId , String email) {
        GifticonEntity gifticonEntity = checkUserAndGifticon(gifticonId, email);

        gifticonRepository.delete(gifticonEntity);

        return null;
    }

    public GifticonResponseDto getGifticonDetail(Long gifticonId, String email) {
        GifticonEntity gifticonEntity = checkUserAndGifticon(gifticonId, email);

        return new GifticonResponseDto(gifticonEntity);
    }

    public GifticonResponseDto updateGifticon(Long gifticonId, GifticonRequestDto gifticonRequestDto, String email) {
        UserEntity user = checkUser(email);
        GifticonEntity gifticonEntity = checkGifticon(gifticonId);
        if (!gifticonEntity.getUser().equals(user))
            throw new ApiException(GifticonMessage.GIFTICON_NOT_BELONG_TO_USER);

        GifticonEntity updatedGifticon = GifticonEntity.builder()
                .name(gifticonRequestDto.getName())
                .barcodeNumber(gifticonRequestDto.getBarcodeNumber())
                .gifticonImagePath(gifticonRequestDto.getGifticonImagePath())
                .exchangePlace(gifticonRequestDto.getExchangePlace())
                .dueDate(gifticonRequestDto.getDueDate())
                .gifticonType(gifticonRequestDto.getGifticonType())
                .orderNumber(gifticonRequestDto.getOrderNumber())
                .status(gifticonEntity.getStatus())
                .usedDate(gifticonEntity.getUsedDate())
                .user(user)
                .build();

        updatedGifticon = gifticonRepository.save(updatedGifticon);

        return new GifticonResponseDto(updatedGifticon);
    }

    private GifticonEntity checkUserAndGifticon(Long gifticonId, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(UserMessage.USER_NOT_FOUND));
        return gifticonRepository.findById(gifticonId).orElseThrow(() -> new ApiException(GifticonMessage.GIFTICON_NOT_FOUND) );
    }
    private GifticonEntity checkGifticon(Long gifticonId) {
        return gifticonRepository.findById(gifticonId).orElseThrow(() -> new ApiException(GifticonMessage.GIFTICON_NOT_FOUND) );
    }
    private UserEntity checkUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ApiException(UserMessage.USER_NOT_FOUND));
    }
}
