package com.gdsc.moa.global.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TeamMessage implements ResponseMessage {
    TEAM_CREATE_SUCCESS("팀생성 성공",HttpStatus.OK),;
    private final String message;
    private final HttpStatus status;
}
