package com.gdsc.moa.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gdsc.moa.domain.user.dto.LogoutRequest;
import com.gdsc.moa.global.dto.MoaApiResponse;
import com.gdsc.moa.global.jwt.dto.TokenResponse;
import com.gdsc.moa.domain.user.service.AuthService;
import com.gdsc.moa.global.jwt.oauth.UserInfo;
import com.gdsc.moa.global.message.UserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @GetMapping("/kakaologin")
    public MoaApiResponse<TokenResponse> kakaoLogin(String accessToken) throws JsonProcessingException {
        TokenResponse response = authService.kakaoLogin(accessToken);
        return MoaApiResponse.createResponse(response, UserMessage.LOGIN_SUCCESS);
    }

    @PostMapping("/token")
    public MoaApiResponse<TokenResponse> reissue(@AuthenticationPrincipal UserInfo user, @RequestBody LogoutRequest logoutRequest){
        TokenResponse response = authService.reissue(user.getEmail(), logoutRequest);
        return MoaApiResponse.createResponse(response, UserMessage.REISSUE_SUCCESS);
    }

    //로그아웃

    //회원 탈퇴

    //닉네임 변경

}


