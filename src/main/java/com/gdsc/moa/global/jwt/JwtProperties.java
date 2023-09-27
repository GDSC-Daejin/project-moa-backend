package com.gdsc.moa.global.jwt;

public interface JwtProperties {
    String SECRET = "Popcon";
    int EXPIRATION_TIME = 864000000; // 10일 (밀리초 단위)
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}