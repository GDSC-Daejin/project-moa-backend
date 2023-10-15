package com.gdsc.moa.global.exception;

import com.gdsc.moa.global.message.ResponseMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException{
    private final ResponseMessage responseMessage;
}
