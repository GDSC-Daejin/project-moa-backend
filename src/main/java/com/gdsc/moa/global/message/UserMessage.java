package com.gdsc.moa.global.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserMessage implements ResponseMessage {
    LOGIN_SUCCESS("로그인 성공", HttpStatus.OK),
    USER_NOT_FOUND("유저가 없습니다.", HttpStatus.BAD_REQUEST),
    REISSUE_SUCCESS("토큰 재발급이 완료되었습니다.", HttpStatus.OK),
    REFRESH_TOKEN_INVALID("리프레시 토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    LOGOUT_SUCCESS("로그아웃에 성공했습니다.", HttpStatus.OK),
    DELETE_USER_SUCCESS("회원 탈퇴에 성공했습니다.", HttpStatus.OK),
    NICKNAME_UPDATE_SUCCESS("닉네임 변경에 성공했습니다", HttpStatus.OK),
    USER_INFO_GET_SUCCESS("유저 정보 조회에 성공했습니다.", HttpStatus.OK);

    private final String message;
    private final HttpStatus status;
    }
