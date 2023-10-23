package com.gdsc.moa.domain.gifticon.dto.response;

import com.gdsc.moa.domain.category.dto.CategoryResponseDto;
import com.gdsc.moa.domain.category.entity.CategoryEntity;
import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import com.gdsc.moa.domain.gifticon.entity.GifticonType;
import com.gdsc.moa.domain.gifticon.entity.Status;
import com.gdsc.moa.domain.user.dto.AuthorDto;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class GifticonResponseDto {
    private Long id;
    private String name;
    private String barcodeNumber;
    private String gifticonImagePath;
    private String exchangePlace;
    private Date dueDate;
    private GifticonType gifticonType;
    private String orderNumber;
    private Status status;
    private Date usedDate;
    private AuthorDto author;
    private CategoryResponseDto category;

    public GifticonResponseDto(GifticonEntity savedGifticon) {
        this.id = savedGifticon.getId();
        this.name = savedGifticon.getName();
        this.barcodeNumber = savedGifticon.getBarcodeNumber();
        this.gifticonImagePath = savedGifticon.getGifticonImagePath();
        this.exchangePlace = savedGifticon.getExchangePlace();
        this.dueDate = savedGifticon.getDueDate();
        this.gifticonType = savedGifticon.getGifticonType();
        this.orderNumber = savedGifticon.getOrderNumber();
        this.status = savedGifticon.getStatus();
        this.usedDate = savedGifticon.getUsedDate();
        this.author = new AuthorDto(
                savedGifticon.getUser().getId(),
                savedGifticon.getUser().getNickname(),
                savedGifticon.getUser().getProfileImageUrl());
        this.category = new CategoryResponseDto(
                savedGifticon.getCategory().getId(),
                savedGifticon.getCategory().getCategoryName());
    }

}
