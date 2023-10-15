package com.gdsc.moa.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialType {
    KAKAO("kakao");
    private final String type;
}