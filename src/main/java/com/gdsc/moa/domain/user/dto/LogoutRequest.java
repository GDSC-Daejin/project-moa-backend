package com.gdsc.moa.domain.user.dto;

public record LogoutRequest(
        String refreshToken
) {
}
