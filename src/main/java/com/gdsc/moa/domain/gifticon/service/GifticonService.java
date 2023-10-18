package com.gdsc.moa.domain.gifticon.service;

import com.gdsc.moa.domain.gifticon.dto.request.GifticonRequestDto;
import com.gdsc.moa.domain.gifticon.dto.response.GifticonResponseDto;
import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import com.gdsc.moa.domain.gifticon.entity.Status;
import com.gdsc.moa.domain.gifticon.repository.GifticonRepository;
import com.gdsc.moa.domain.user.entity.UserEntity;
import com.gdsc.moa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GifticonService {
    private final UserRepository userRepository;
    private final GifticonRepository gifticonRepository;

    //Gifticon 생성
    // TODO: 10/15/23 이미지 저장하는 방식 처리하기
    public GifticonResponseDto createGifticon(GifticonRequestDto gifticonRequestDto, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. email=" + email));
        GifticonEntity gifticonEntity = GifticonEntity.builder()
                .name(gifticonRequestDto.getName())
                .barcode_number(gifticonRequestDto.getBarcode_number())
                .gifticon_image_url(gifticonRequestDto.getGifticon_image_url())
                .exchange_place(gifticonRequestDto.getExchange_place())
                .due_date(gifticonRequestDto.getDue_date())
                .gifticon_type(gifticonRequestDto.getGifticon_type())
                .order_number(gifticonRequestDto.getOrder_number())
                .status(Status.AVAILABLE)
                .used_date(null)
                .user_id(user)
                .build();
        GifticonEntity savedGifticon = gifticonRepository.save(gifticonEntity);

        return new GifticonResponseDto(savedGifticon);
    }

    public GifticonResponseDto deleteGifticon(Long gifticonId , String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. email=" + email));
        GifticonEntity gifticonEntity = gifticonRepository.findById(gifticonId).orElseThrow(() -> new IllegalArgumentException("해당 기프티콘이 없습니다. id=" + gifticonId));

        gifticonRepository.delete(gifticonEntity);

        return null;
    }

    public GifticonResponseDto getGifticonDetail(Long gifticonId, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. email=" + email));
        GifticonEntity gifticonEntity = gifticonRepository.findById(gifticonId).orElseThrow(() -> new IllegalArgumentException("해당 기프티콘이 없습니다. id=" + gifticonId));

        return new GifticonResponseDto(gifticonEntity);
    }
}
