package com.gdsc.moa.domain.user.service;

import com.gdsc.moa.domain.user.entity.UserEntity;
import com.gdsc.moa.domain.user.info.impl.KakaoOAuth2UserInfo;
import com.gdsc.moa.domain.user.repository.UserRepository;
import com.gdsc.moa.global.jwt.dto.KakaoIdTokenDecoder;
import com.gdsc.moa.global.jwt.dto.KakaoUserResponse;
import com.gdsc.moa.global.jwt.dto.TokenResponse;
import com.gdsc.moa.global.jwt.TokenProvider;
import com.gdsc.moa.domain.user.entity.RoleType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public TokenResponse kakaoLogin(String idToken) {
        // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장
        return saveUserAndGetToken(idToken);
    }

public TokenResponse saveUserAndGetToken(String idToken)  {
        KakaoOAuth2UserInfo profile = getUserInfoByToken(idToken);

        UserEntity user = profile.createUserEntity();
        userRepository.save(user);

        return createToken(user);
    }


    private TokenResponse createToken(UserEntity user) {
        return tokenProvider.generateJwtToken(user.getEmail(), user.getNickname(), RoleType.MEMBER);
    }

    private KakaoOAuth2UserInfo getUserInfoByToken(String accessToken) {
        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        // Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.

        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        log.info("Kakao API Response: {}", kakaoProfileResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoUserResponse kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoUserResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.info("Kakao API kakaoUserResponse: {}", kakaoProfile);

        //가져온 사용자 정보를 객체로 만들어서 반환
        return new KakaoOAuth2UserInfo(kakaoProfile);
    }

}

