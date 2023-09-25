package com.example.moa.api.jwt.dto;


import lombok.Getter;

@Getter
public class KakaoUserInfo {
    private String email;
    private String nickname;
    private String profileImageUrl;

    public KakaoUserInfo() {
        // 기본 생성자
    }

    public KakaoUserInfo(String email, String nickname, String profileImageUrl) {
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }


}
