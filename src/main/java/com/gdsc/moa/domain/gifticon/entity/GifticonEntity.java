package com.gdsc.moa.domain.gifticon.entity;

import com.gdsc.moa.domain.category.entity.CategoryEntity;
import com.gdsc.moa.domain.gifticon.dto.request.GifticonRequestDto;
import com.gdsc.moa.domain.gifticon.dto.request.GifticonUpdateRequestDto;
import com.gdsc.moa.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "tb_gifticon")
public class GifticonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gifticon_id")
    private Long gifticonId;
    private String name;
    private String barcodeNumber;
    private String gifticonImagePath;
    private String exchangePlace;
    private Date dueDate;
    private String orderNumber;
    @Enumerated(EnumType.STRING)
    private GifticonType gifticonType;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Date usedDate;

    @BatchSize(size = 100)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @BatchSize(size = 100)
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Builder
    public GifticonEntity(String name,
                          String barcodeNumber,
                          String gifticonImagePath,
                          String exchangePlace,
                          Date dueDate,
                          String orderNumber,
                          GifticonType gifticonType,
                          Status status,
                          Date usedDate,
                          UserEntity user) {
        this.name = name;
        this.barcodeNumber = barcodeNumber;
        this.gifticonImagePath = gifticonImagePath;
        this.exchangePlace = exchangePlace;
        this.dueDate = dueDate;
        this.orderNumber = orderNumber;
        this.gifticonType = gifticonType;
        this.status = status;
        this.usedDate = usedDate;
        this.user = user;

    }
    @Builder
    public GifticonEntity(GifticonRequestDto gifticonRequestDto, UserEntity user, CategoryEntity category){
        this.name = gifticonRequestDto.getName();
        this.barcodeNumber = gifticonRequestDto.getBarcodeNumber();
        this.gifticonImagePath = gifticonRequestDto.getGifticonImagePath();
        this.exchangePlace = gifticonRequestDto.getExchangePlace();
        this.dueDate = gifticonRequestDto.getDueDate();
        this.orderNumber = gifticonRequestDto.getOrderNumber();
        this.gifticonType = gifticonRequestDto.getGifticonType();
        this.status = Status.AVAILABLE;
        this.usedDate = null;
        this.user = user;
        this.category = category;
    }
    @Builder
    public GifticonEntity(GifticonUpdateRequestDto gifticonUpdateRequestDto, UserEntity user,CategoryEntity category){
        this.gifticonId = gifticonUpdateRequestDto.getId();
        this.name = gifticonUpdateRequestDto.getName();
        this.barcodeNumber = gifticonUpdateRequestDto.getBarcodeNumber();
        this.gifticonImagePath = gifticonUpdateRequestDto.getGifticonImagePath();
        this.exchangePlace = gifticonUpdateRequestDto.getExchangePlace();
        this.dueDate = gifticonUpdateRequestDto.getDueDate();
        this.orderNumber = gifticonUpdateRequestDto.getOrderNumber();
        this.gifticonType = gifticonUpdateRequestDto.getGifticonType();
        this.status = Status.AVAILABLE;
        this.usedDate = null;
        this.user = user;
        this.category = category;

    }

    public GifticonEntity(GifticonEntity gifticonEntity) {
        this.gifticonId = gifticonEntity.getGifticonId();
        this.name = gifticonEntity.getName();
        this.barcodeNumber = gifticonEntity.getBarcodeNumber();
        this.gifticonImagePath = gifticonEntity.getGifticonImagePath();
        this.exchangePlace = gifticonEntity.getExchangePlace();
        this.dueDate = gifticonEntity.getDueDate();
        this.orderNumber = gifticonEntity.getOrderNumber();
        this.gifticonType = gifticonEntity.getGifticonType();
        this.status = gifticonEntity.getStatus();
        this.usedDate = gifticonEntity.getUsedDate();
        this.user = gifticonEntity.getUser();
        this.category = gifticonEntity.getCategory();
    }


    //TODO: 10/15/23  사용한 유저 map 으로 json형식으로 만들기

    // TODO: 10/15/23 category 생성시 추가
    //private Category category;


}
