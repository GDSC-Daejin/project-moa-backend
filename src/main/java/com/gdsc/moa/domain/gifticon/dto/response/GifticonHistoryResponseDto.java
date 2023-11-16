package com.gdsc.moa.domain.gifticon.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GifticonHistoryResponseDto {
    private Long id;
    private Long usedPrice;
    private Long leftPrice;
    private LocalDateTime usedDate;
}
