package com.gdsc.moa.global.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GifticonMessage implements ResponseMessage {
    GIFTICON_CREATE_SUCCESS("기프티콘 생성 성공", HttpStatus.OK),
    GIFTICON_CREATE_FAIL("기프티콘 생성 실패", HttpStatus.BAD_REQUEST),
    GIFTICON_DELETE_SUCCESS("기프티콘 삭제 성공", HttpStatus.OK),
    GIFTICON_DELETE_FAIL("기프티콘 삭제 실패", HttpStatus.BAD_REQUEST),;
    private final String message;
    private final HttpStatus status;
    }
