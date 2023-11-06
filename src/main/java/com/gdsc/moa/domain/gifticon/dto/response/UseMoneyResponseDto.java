package com.gdsc.moa.domain.gifticon.dto.response;

import com.gdsc.moa.domain.gifticon.entity.GifticonHistoryEntity;
import com.gdsc.moa.domain.user.dto.AuthorDto;
import com.gdsc.moa.domain.user.entity.UserEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UseMoneyResponseDto {
    private Long gifticonHistoryId;
    private Long usedPrice;
    private Long leftPrice;
    private LocalDateTime usedDate;
    private Long gifticonId;
    private AuthorDto usedUser;

    public UseMoneyResponseDto(GifticonHistoryEntity savedHistory) {
        this.gifticonHistoryId = savedHistory.getId();
        this.usedPrice = savedHistory.getUsedPrice();
        this.leftPrice = savedHistory.getLeftPrice();
        this.usedDate = savedHistory.getUsedDate();
        this.gifticonId = savedHistory.getGifticon().getGifticonId();
        this.usedUser = new AuthorDto(
                savedHistory.getUser().getId(),
                savedHistory.getUser().getNickname(),
                savedHistory.getUser().getProfileImageUrl());
    }

    public UseMoneyResponseDto(Long id, Long usedPrice, Long leftPrice, LocalDateTime usedDate, Long gifticonId, UserEntity user) {
        this.gifticonHistoryId = id;
        this.usedPrice = usedPrice;
        this.leftPrice = leftPrice;
        this.usedDate = usedDate;
        this.gifticonId = gifticonId;
        this.usedUser = new AuthorDto(
                user.getId(),
                user.getNickname(),
                user.getProfileImageUrl());
    }
}
