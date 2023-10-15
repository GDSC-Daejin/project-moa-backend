package com.gdsc.moa.global.dto;

import com.gdsc.moa.global.message.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MoaApiResponse<T> {
    T data;
    String message;
    String code;

    public static <G> MoaApiResponse<G> createResponse(G data, ResponseMessage responseMessage) {
        return new MoaApiResponse<>(data, responseMessage.getMessage(), responseMessage.toString());
    }
}
