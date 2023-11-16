package com.gdsc.moa.domain.user.controller;

import com.gdsc.moa.domain.user.dto.FCMNotificationRequest;
import com.gdsc.moa.domain.user.service.FcmService;
import com.gdsc.moa.global.dto.MoaApiResponse;
import com.gdsc.moa.global.message.FcmMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FcmController {
    private final FcmService fcmService;

    @PostMapping("/fcm")
    public MoaApiResponse<?> sendFcmMessage(FCMNotificationRequest request) {
        fcmService.sendMessageTo(request);
        return MoaApiResponse.createResponse(null, FcmMessage.FCM_SEND_SUCCESS);
    }
}
