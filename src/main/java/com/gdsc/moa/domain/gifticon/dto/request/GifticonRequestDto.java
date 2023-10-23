package com.gdsc.moa.domain.gifticon.dto.request;


import com.gdsc.moa.domain.gifticon.entity.GifticonType;
import lombok.Getter;

import java.util.Date;

@Getter
public class GifticonRequestDto {
    private String name;
    private String barcodeNumber;
    private String gifticonImagePath;
    private String exchangePlace;
    private Date dueDate;
    private String orderNumber;
    private GifticonType gifticonType;
}
