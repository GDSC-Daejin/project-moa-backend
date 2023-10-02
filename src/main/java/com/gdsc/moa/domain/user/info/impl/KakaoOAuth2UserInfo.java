package com.gdsc.moa.domain.user.info.impl;

import com.gdsc.moa.domain.user.entity.RoleType;
import com.gdsc.moa.domain.user.entity.SocialType;
import com.gdsc.moa.domain.user.entity.Status;
import com.gdsc.moa.domain.user.entity.UserEntity;
import com.gdsc.moa.domain.user.info.OAuth2UserInfo;
import com.gdsc.moa.global.jwt.dto.KakaoUserResponse;
import lombok.Getter;

@Getter
public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
    private String id;
    private String nickname;
    private String email;
    private String imageUrl;

    private final SocialType socialType = SocialType.KAKAO;

    public UserEntity createUserEntity(KakaoUserResponse kakaoProfile) {
        new UserEntity();
        return UserEntity.builder()
                .email(kakaoProfile.getKakao_account().getEmail())
                .nickname(kakaoProfile.getKakao_account().getProfile().getNickname())
                .profileImageUrl(kakaoProfile.getProperties().getProfile_image())
                .userRole(RoleType.MEMBER)
                .socialType(SocialType.KAKAO)
                .status(Status.ACTIVE)
                .build();
    }

    public KakaoOAuth2UserInfo(){
    }

    public KakaoOAuth2UserInfo(KakaoUserResponse kakaoProfile) {
        this.email = kakaoProfile.getKakao_account().getEmail();
        this.nickname = kakaoProfile.getKakao_account().getProfile().getNickname();
        this.imageUrl = kakaoProfile.getProperties().getProfile_image();
    }
}
