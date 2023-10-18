package com.gdsc.moa.domain.gifticon.dto.response;

import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import com.gdsc.moa.domain.gifticon.entity.GifticonType;
import com.gdsc.moa.domain.gifticon.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Date;

@AllArgsConstructor
@Builder
public class GifticonResponseDto {
    private String name;
    private String barcodeNumber;
    private String gifticonImagePath;
    private String exchangePlace;
    private Date dueDate;
    private GifticonType gifticonType;
    private String orderNumber;
    private Status status;
    private Date usedDate;
    private String author;

    public GifticonResponseDto(GifticonEntity savedGifticon) {
        this.name = savedGifticon.getName();
        this.barcodeNumber = savedGifticon.getBarcodeNumber();
        this.gifticonImagePath = savedGifticon.getGifticonImagePath();
        this.exchangePlace = savedGifticon.getExchangePlace();
        this.dueDate = savedGifticon.getDueDate();
        this.gifticonType = savedGifticon.getGifticonType();
        this.orderNumber = savedGifticon.getOrderNumber();
        this.status = savedGifticon.getStatus();
        this.usedDate = savedGifticon.getUsedDate();
        this.author = savedGifticon.getUser().getNickname();

    }
}
