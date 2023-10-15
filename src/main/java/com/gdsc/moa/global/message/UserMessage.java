package com.gdsc.moa.global.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserMessage implements ResponseMessage {
    LOGIN_SUCCESS("로그인 성공", HttpStatus.OK);

    private final String message;
    private final HttpStatus status;
}
