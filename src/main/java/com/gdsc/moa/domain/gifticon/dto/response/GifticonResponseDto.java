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
    private String barcode_number;
    private String gifticon_image_url;
    private String exchange_place;
    private Date due_date;
    private GifticonType gifticon_type;
    private String order_number;
    private Status status;
    private Date used_date;
    private String author;

    public GifticonResponseDto(GifticonEntity savedGifticon) {
        this.name = savedGifticon.getName();
        this.barcode_number = savedGifticon.getBarcode_number();
        this.gifticon_image_url = savedGifticon.getGifticon_image_url();
        this.exchange_place = savedGifticon.getExchange_place();
        this.due_date = savedGifticon.getDue_date();
        this.gifticon_type = savedGifticon.getGifticon_type();
        this.order_number = savedGifticon.getOrder_number();
        this.status = savedGifticon.getStatus();
        this.used_date = savedGifticon.getUsed_date();
        this.author = savedGifticon.getUser_id().getNickname();

    }
}
