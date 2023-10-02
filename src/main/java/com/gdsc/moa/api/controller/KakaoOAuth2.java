package com.gdsc.moa.api.controller;

import com.gdsc.moa.domain.user.info.impl.KakaoOAuth2UserInfo;
import com.gdsc.moa.global.jwt.dto.KakaoUserResponse;
import com.gdsc.moa.global.jwt.dto.TokenResponse;
import com.google.gson.Gson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoOAuth2 {

    public KakaoOAuth2UserInfo getUserInfo(String authorizedCode) {
        System.out.println("getUserInfo 호출");
        // 인가코드 -> 액세스 토큰
        String accessToken = getAccessToken(authorizedCode);
        // 액세스 토큰 -> 카카오 사용자 정보
        KakaoOAuth2UserInfo userInfo = getUserInfoByToken(accessToken);

        return userInfo;
    }

    private String getAccessToken(String authorizedCode) {
        System.out.println("getAccessToken 호출");
        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "0eba27bba37e18dbbe15cc3dbfd85106");
        params.add("redirect_uri", "http://localhost:8080/user/kakao/callback");
        params.add("code", authorizedCode);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        // Http 요청하기, Post방식으로, 그리고 response 변수의 응답 받음
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // JSON -> 액세스 토큰 파싱
        String tokenJson = response.getBody();
        Gson gson = new Gson();
        TokenResponse tokenResponse = gson.fromJson(tokenJson, TokenResponse.class);
        String accessToken = tokenResponse.accessToken();

        return accessToken;
    }

    //토큰을 통해 사용자 정보 가져오기
    private KakaoOAuth2UserInfo getUserInfoByToken(String accessToken) {
        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        // Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        Gson gson = new Gson();
        KakaoUserResponse kakaoUserResponse = gson.fromJson(response.getBody(), KakaoUserResponse.class);

        //가져온 사용자 정보를 객체로 만들어서 반환
        return new KakaoOAuth2UserInfo(kakaoUserResponse);
//        return new KakaoUserInfo(kakaoUserResponse.getKakao_account().getEmail(), kakaoUserResponse.getProperties().getNickname(), kakaoUserResponse.getProperties().getProfile_image());
    }
}