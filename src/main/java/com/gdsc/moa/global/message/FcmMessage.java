package com.gdsc.moa.global.message;

import org.springframework.http.HttpStatus;

public enum FcmMessage implements ResponseMessage{
    FCM_TOKEN_NOT_FOUND("토큰을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    GOOGLE_REQUEST_TOKEN_ERROR("구글 OAuth 2.0 토큰 요청에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    CONVERTING_JSON_ERROR("JSON 변환에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    FIREBASE_REQUEST_ERROR("Firebase 메시지 요청에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    FCM_SEND_SUCCESS("FCM 메시지 전송에 성공했습니다", HttpStatus.OK);


    private final String message;
    private final HttpStatus status;

    FcmMessage(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
