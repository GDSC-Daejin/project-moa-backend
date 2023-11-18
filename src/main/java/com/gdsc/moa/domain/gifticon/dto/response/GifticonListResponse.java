package com.gdsc.moa.domain.gifticon.dto.response;

import com.gdsc.moa.domain.category.dto.CategoryResponseDto;
import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import com.gdsc.moa.domain.gifticon.entity.GifticonType;
import com.gdsc.moa.domain.gifticon.entity.Status;
import com.gdsc.moa.domain.user.dto.AuthorDto;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GifticonListResponse {
    private Long id;
    private String name;
    private String gifticonImagePath;
    private String exchangePlace;
    private Date dueDate;
    private GifticonType gifticonType;
    private String gifticonMoney;
    private Status status;
    private Date usedDate;
    private AuthorDto author;
    private CategoryResponseDto category;
    private List<GifticonHistoryResponseDto> gifticonHistories;

    @Builder
    public GifticonListResponse(GifticonEntity gifticonEntity){
        this.id = gifticonEntity.getGifticonId();
        this.name = gifticonEntity.getName();
        this.gifticonImagePath = gifticonEntity.getGifticonImagePath();
        this.exchangePlace = gifticonEntity.getExchangePlace();
        this.dueDate = gifticonEntity.getDueDate();
        this.gifticonType = gifticonEntity.getGifticonType();
        this.gifticonMoney = gifticonEntity.getGifticonMoney();
        this.status = gifticonEntity.getStatus();
        this.usedDate = gifticonEntity.getUsedDate();
        this.author = AuthorDto.builder()
                .id(gifticonEntity.getUser().getId())
                .nickname(gifticonEntity.getUser().getNickname())
                .profileImageUrl(gifticonEntity.getUser().getProfileImageUrl())
                .build();
        this.category = CategoryResponseDto.builder()
                .id(gifticonEntity.getCategory().getId())
                .categoryName(gifticonEntity.getCategory().getCategoryName())
                .build();
        this.gifticonHistories = gifticonEntity.getGifticonHistoryEntityList().stream()
                .map(historyEntity -> GifticonHistoryResponseDto.builder()
                        .id(historyEntity.getId())
                        .usedPrice(historyEntity.getUsedPrice())
                        .leftPrice(historyEntity.getLeftPrice())
                        .usedDate(historyEntity.getUsedDate())
                        .build())
                .collect(Collectors.toList());
    }
}

