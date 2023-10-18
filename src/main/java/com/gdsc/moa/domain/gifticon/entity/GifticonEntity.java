package com.gdsc.moa.domain.gifticon.entity;

import com.gdsc.moa.domain.user.entity.UserEntity;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.util.Json;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "tb_gifticon")
public class GifticonEntity {
    @Id
    @GeneratedValue(generator = "gifticon_seq")
    private Long id;
    private String name;
    private String barcode_number;
    private String gifticon_image_url;
    private String exchange_place;
    private Date due_date;
    private String order_number;
    @Enumerated(EnumType.STRING)
    private GifticonType gifticon_type;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Date used_date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user_id;

    @Builder
    public GifticonEntity(String name,
                          String barcode_number,
                          String gifticon_image_url,
                          String exchange_place,
                          Date due_date,
                          String order_number,
                          GifticonType gifticon_type,
                          Status status,
                          Date used_date,
                          UserEntity user_id) {
        this.name = name;
        this.barcode_number = barcode_number;
        this.gifticon_image_url = gifticon_image_url;
        this.exchange_place = exchange_place;
        this.due_date = due_date;
        this.order_number = order_number;
        this.gifticon_type = gifticon_type;
        this.status = status;
        this.used_date = used_date;
        this.user_id = user_id;
    }


    //TODO: 10/15/23  사용한 유저 map 으로 json형식으로 만들기

    // TODO: 10/15/23 category 생성시 추가
    //private Category category;


}
