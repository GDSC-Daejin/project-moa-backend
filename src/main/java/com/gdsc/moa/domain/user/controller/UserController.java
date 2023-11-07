package com.gdsc.moa.domain.user.controller;

import com.gdsc.moa.domain.user.dto.AuthorDto;
import com.gdsc.moa.domain.user.dto.NicknameRequest;
import com.gdsc.moa.domain.user.service.UserService;
import com.gdsc.moa.global.dto.MoaApiResponse;
import com.gdsc.moa.global.jwt.oauth.UserInfo;
import com.gdsc.moa.global.message.UserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/user")
    public MoaApiResponse<?> updateNickName(@AuthenticationPrincipal UserInfo user,
                                            @RequestBody NicknameRequest nicknameRequest) {
        userService.updateNickname(user.getEmail(), nicknameRequest.nickname());
        return MoaApiResponse.createResponse(null, UserMessage.NICKNAME_UPDATE_SUCCESS);
    }
    @GetMapping("/user/me")
    public MoaApiResponse<AuthorDto> getUserInfo(@AuthenticationPrincipal UserInfo user) {
        AuthorDto response = userService.getUserInfo(user.getEmail());
        return MoaApiResponse.createResponse(response, UserMessage.USER_INFO_GET_SUCCESS);
    }

}
