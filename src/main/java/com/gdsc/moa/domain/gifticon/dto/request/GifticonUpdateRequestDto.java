package com.gdsc.moa.domain.gifticon.dto.request;

import com.gdsc.moa.domain.gifticon.entity.GifticonType;
import lombok.Getter;

import java.util.Date;

@Getter
public class GifticonUpdateRequestDto {
    private Long id;
    private String name;
    private String barcodeNumber;
    private String gifticonImagePath;
    private String exchangePlace;
    private Date dueDate;
    private String orderNumber;
    private GifticonType gifticonType;
    private String gifticonMoney;
    private Long categoryId;
}
