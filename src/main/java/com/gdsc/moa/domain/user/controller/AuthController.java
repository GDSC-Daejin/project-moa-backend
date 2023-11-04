package com.gdsc.moa.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gdsc.moa.domain.user.dto.LogoutRequest;
import com.gdsc.moa.global.dto.MoaApiResponse;
import com.gdsc.moa.global.jwt.dto.TokenResponse;
import com.gdsc.moa.domain.user.service.AuthService;
import com.gdsc.moa.global.jwt.oauth.UserInfo;
import com.gdsc.moa.global.message.UserMessage;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "토큰 재발급 API", description = "AccessToken 만료 시 RefreshToken을 가지고 AccessToken, RefreshToken을 재발급 받을 수 있습니다.")
    @PostMapping("/token")
    public MoaApiResponse<TokenResponse> reissue(@AuthenticationPrincipal UserInfo user,
                                                 @RequestBody LogoutRequest logoutRequest){
        TokenResponse response = authService.reissue(user.getEmail(), logoutRequest);
        return MoaApiResponse.createResponse(response, UserMessage.REISSUE_SUCCESS);
    }

    @Operation(summary = "로그아웃 API")
    @PostMapping("/auth/user/logout")
    public MoaApiResponse<String> logout(@AuthenticationPrincipal UserInfo user,
                                         @RequestBody LogoutRequest logoutRequest){
        authService.logout(user.getEmail(), logoutRequest);
        return MoaApiResponse.createResponse(null, UserMessage.LOGOUT_SUCCESS);
    }

    @Operation(summary = "회원 탈퇴 API")
    @PostMapping("/auth/user")
    public MoaApiResponse<String> deleteUser(@AuthenticationPrincipal UserInfo user){
        authService.deleteUser(user.getEmail());
        return MoaApiResponse.createResponse(null, UserMessage.DELETE_USER_SUCCESS);
    }

}


