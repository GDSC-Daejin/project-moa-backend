package com.example.moa.api.jwt.dto;


public record TokenResponse(
        String grantType,
        String accessToken,
        String refreshToken,
        Long accessTokenExpiresIn
) {

}