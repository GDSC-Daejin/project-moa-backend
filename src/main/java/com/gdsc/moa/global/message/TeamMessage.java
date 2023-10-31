package com.gdsc.moa.global.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TeamMessage implements ResponseMessage {
    TEAM_CREATE_SUCCESS("팀생성 성공",HttpStatus.OK),
    TEAM_NOT_FOUND("팀을 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
    TEAM_JOIN_SUCCESS("팀 가입 성공",HttpStatus.OK),
    TEAM_ALREADY_JOINED("이미 가입된 팀입니다.",HttpStatus.BAD_REQUEST),
    TEAM_GET_SUCCESS("팀 조회 성공",HttpStatus.OK),;
    private final String message;
    private final HttpStatus status;
}
