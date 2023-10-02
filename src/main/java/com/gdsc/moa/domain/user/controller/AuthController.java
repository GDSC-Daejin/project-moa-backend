package com.gdsc.moa.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gdsc.moa.global.dto.MoaApiResponse;
import com.gdsc.moa.global.jwt.dto.TokenResponse;
import com.gdsc.moa.domain.user.service.AuthService;
import com.gdsc.moa.global.message.UserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    public MoaApiResponse<TokenResponse> kakaoLogin(String code) throws JsonProcessingException {
        // code는 카카오 서버로부터 받은 인가 코드
        // 넘어온 인가 코드를 통해 access_token 발급
        TokenResponse response = authService.kakaoLogin(code);

        return MoaApiResponse.createResponse(response, UserMessage.LOGIN_SUCCESS);
    }
}


