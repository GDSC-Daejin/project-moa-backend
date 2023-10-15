package com.gdsc.moa.global.jwt.dto;


public record TokenResponse(
        String grantType,
        String accessToken,
        String refreshToken,
        Long accessTokenExpiresIn
) {

}