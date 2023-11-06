package com.gdsc.moa.domain.gifticon.dto.response;

import com.gdsc.moa.domain.gifticon.entity.GifticonHistoryEntity;
import com.gdsc.moa.domain.user.dto.AuthorDto;
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
}
