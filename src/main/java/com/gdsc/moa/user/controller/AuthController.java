package com.gdsc.moa.user.controller;

import com.gdsc.moa.global.jwt.dto.OauthToken;
import com.gdsc.moa.global.jwt.dto.TokenResponse;
import com.gdsc.moa.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @Value("${url.base}")
    private String baseUrl;

    @Value("${social.kakao.client-id}")
    private String kakaoClientId;

    @Value("${social.kakao.redirect}")
    private String kakaoRedirectUri;



    @GetMapping("/user/kakao")
    public ResponseEntity<TokenResponse> kakaoLogin(String code)   {
        // code는 카카오 서버로부터 받은 인가 코드
        log.info("kakaoLogin");
        // 넘어온 인가 코드를 통해 access_token 발급
        OauthToken oauthToken = authService.getKakaoOauthToken(code);
        // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장
        TokenResponse response = authService.SaveUserAndGetToken(oauthToken.getAccess_token());

        return ResponseEntity.ok(response);
    }
}


