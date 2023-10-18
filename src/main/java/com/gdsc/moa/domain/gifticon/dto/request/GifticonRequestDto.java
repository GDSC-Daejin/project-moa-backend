package com.gdsc.moa.domain.gifticon.dto.request;


import com.gdsc.moa.domain.gifticon.entity.GifticonType;
import com.gdsc.moa.domain.gifticon.entity.Status;
import lombok.Getter;

import java.util.Date;

@Getter
public class GifticonRequestDto {
    private String name;
    private String barcode_number;
    private String gifticon_image_url;
    private String exchange_place;
    private Date due_date;
    private String order_number;
    private GifticonType gifticon_type;
}
