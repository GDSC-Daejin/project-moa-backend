package com.gdsc.moa.domain.user.dto;

public record FCMNotificationRequest(
        Long targetUserId,
        String title,
        String body
) { }
