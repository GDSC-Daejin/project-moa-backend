package com.gdsc.moa.domain.gifticon.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GifticonType {
    GENERAL("일반"),
    MONEY("금액권");
    private final String type;
}
