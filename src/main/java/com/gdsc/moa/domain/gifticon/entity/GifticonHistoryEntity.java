package com.gdsc.moa.domain.gifticon.entity;

import com.gdsc.moa.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class GifticonHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gifticon_history_id")
    private Long id;
    private Long usedPrice;
    private Long leftPrice;
    private LocalDateTime usedDate;
    @ManyToOne
    @JoinColumn(name = "gifticon_id")
    private GifticonEntity gifticon;
    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    public GifticonHistoryEntity(UserEntity user, GifticonEntity gifticonEntity, Long money) {
        this.user = user;
        this.gifticon = gifticonEntity;
        this.usedPrice = money;
        Long gifticonMoney = Long.parseLong(gifticonEntity.getGifticonMoney());
        this.leftPrice = gifticonMoney - money;// 초기에는 남은 가격을 0으로 설정
        this.usedDate = LocalDateTime.now(); // 현재 날짜 및 시간으로 설정
    }

    public GifticonHistoryEntity(UserEntity user, GifticonEntity gifticonEntity, Long leftPrice, Long money) {
        this.user = user;
        this.gifticon = gifticonEntity;
        this.usedPrice = money;
        this.leftPrice = leftPrice - money;
        this.usedDate = LocalDateTime.now();
    }
}
