package com.gdsc.moa.domain.user.service;

import static com.gdsc.moa.global.message.FcmMessage.CONVERTING_JSON_ERROR;
import static com.gdsc.moa.global.message.FcmMessage.FCM_TOKEN_NOT_FOUND;
import static com.gdsc.moa.global.message.FcmMessage.FIREBASE_REQUEST_ERROR;
import static com.gdsc.moa.global.message.FcmMessage.GOOGLE_REQUEST_TOKEN_ERROR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.moa.domain.user.dto.FCMNotificationRequest;
import com.gdsc.moa.domain.user.dto.FcmMessageDto;
import com.gdsc.moa.domain.user.entity.FcmTokenEntity;
import com.gdsc.moa.domain.user.repository.FcmTokenRepository;
import com.gdsc.moa.global.exception.ApiException;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class FcmService {
    private static final String PREFIX_ACCESS_TOKEN = "Bearer ";
    @Value("${fcm.api.url}")
    private String PREFIX_FCM_REQUEST_URL;
    @Value("${firebase.config.path}")
    private String FIREBASE_CONFIG_PATH;
    private final FcmTokenRepository fcmTokenRepository;
    private final ObjectMapper objectMapper;
    private RestTemplate restTemplete;
    public void sendMessageTo(FCMNotificationRequest fcmNotificationRequest) {
        setRestTemplete();
        //알림 요청 받는 사람의 FCM Token이 존재하는지 확인
        final FcmTokenEntity fcmToken = fcmTokenRepository.findById(fcmNotificationRequest.targetUserId())
                .orElseThrow(() -> new ApiException(FCM_TOKEN_NOT_FOUND));
        //메시지 만들기
        final String message = makeMessage(fcmToken.getToken(),
                                            fcmNotificationRequest.title(),
                                            fcmNotificationRequest.body(),
                                            fcmNotificationRequest.targetUserId().toString());

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, PREFIX_ACCESS_TOKEN + getAccessToken());

        final HttpEntity<String> httpEntity = new HttpEntity<>(message, httpHeaders);

        final String fcmRequestUrl = PREFIX_FCM_REQUEST_URL;

        final ResponseEntity<String> exchange = restTemplete.exchange(
                fcmRequestUrl,
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        if (exchange.getStatusCode().isError()) {
            throw new ApiException(FIREBASE_REQUEST_ERROR);
        }
    }
    private void setRestTemplete() {
        restTemplete = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }

    private String makeMessage(String targetToken, String title, String body, String name) {
        FcmMessageDto fcmMessage = FcmMessageDto.builder()
                .message(
                        FcmMessageDto.Message.builder()
                        .token(targetToken)
                        .notification(
                                FcmMessageDto.Notification.builder()
                                        .title(title)
                                        .body(body)
                                        .build()
                        )
                        .data(
                                FcmMessageDto.Data.builder()
                                        .name(name)
                                        .build()
                        ).build())
                .validateOnly(false)
                .build();
        try {
            return objectMapper.writeValueAsString(fcmMessage);
        } catch (JsonProcessingException e) {
            throw new ApiException(CONVERTING_JSON_ERROR);
        }
    }

    private String getAccessToken() {
        try {
            final GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream())
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

            googleCredentials.refreshIfExpired();

            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new ApiException(GOOGLE_REQUEST_TOKEN_ERROR);
        }
    }

}
