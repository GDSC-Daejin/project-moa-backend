package com.gdsc.moa.domain.user.service;

import com.gdsc.moa.domain.user.entity.UserEntity;
import com.gdsc.moa.domain.user.info.impl.KakaoOAuth2UserInfo;
import com.gdsc.moa.domain.user.repository.UserRepository;
import com.gdsc.moa.global.jwt.dto.KakaoUserResponse;
import com.gdsc.moa.global.jwt.dto.TokenResponse;
import com.gdsc.moa.global.jwt.TokenProvider;
import com.gdsc.moa.domain.user.entity.RoleType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        KakaoOAuth2UserInfo profile = getUserInfoByToken(idToken);
        if (userRepository.existsByEmail(profile.getEmail())) {
            // 이미 등록된 사용자의 경우 토큰을 생성하고 반환
            UserEntity user = userRepository.findByEmail(profile.getEmail()).orElse(null);
            return createToken(user);
        } else {// 사용자가 존재하지 않는 경우 새로운 사용자 생성
            UserEntity newUser = profile.createUserEntity();
            userRepository.save(newUser);
            // 새로운 사용자에게 토큰을 생성하고 반환
            return createToken(newUser);
        }
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

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoUserResponse kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoUserResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //가져온 사용자 정보를 객체로 만들어서 반환
        return new KakaoOAuth2UserInfo(kakaoProfile);
    }

}

