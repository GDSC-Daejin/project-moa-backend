package com.gdsc.moa.domain.user.service;

import com.gdsc.moa.domain.user.entity.RefreshTokenEntity;
import com.gdsc.moa.domain.user.entity.UserEntity;
import com.gdsc.moa.domain.user.info.impl.KakaoOAuth2UserInfo;
import com.gdsc.moa.domain.user.repository.RefreshTokenRepository;
import com.gdsc.moa.domain.user.repository.UserRepository;
import com.gdsc.moa.global.jwt.dto.KakaoUserResponse;
import com.gdsc.moa.global.jwt.dto.TokenResponse;
import com.gdsc.moa.global.jwt.TokenProvider;
import com.gdsc.moa.domain.user.entity.RoleType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    public TokenResponse kakaoLogin(String accessToken) {
        KakaoOAuth2UserInfo profile = getUserInfoByToken(accessToken);

        UserEntity user = userRepository.findByEmail(profile.getEmail())
                .orElse(profile.createUserEntity());
        userRepository.save(user);

        return createToken(user);
    }

    private TokenResponse createToken(UserEntity user) {
        TokenResponse tokenResponse = tokenProvider.generateJwtToken(user.getEmail(), user.getNickname(), RoleType.MEMBER);
        RefreshTokenEntity refreshTokenEntity = checkExistingRefreshToken(user.getId());

        if(refreshTokenEntity == null) {
            RefreshTokenEntity newRefreshTokenEntity = new RefreshTokenEntity(
                    user.getId(),
                    tokenResponse.refreshToken()
            );
            refreshTokenRepository.save(newRefreshTokenEntity);
        }
        else {
            refreshTokenEntity.updateRefreshToken(tokenResponse.refreshToken());
        }

        return tokenResponse;
    }

    private RefreshTokenEntity checkExistingRefreshToken(Long userId) {
        Optional<RefreshTokenEntity> result = refreshTokenRepository.findByUserId(userId);
        return result.orElse(null);
    }

    private KakaoOAuth2UserInfo getUserInfoByToken(String accessToken) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().responseTimeout(Duration.ofSeconds(5))))
                .build();

        KakaoUserResponse kakaoProfile = webClient.post()
                .uri("/v2/user/me")
                .retrieve()
                .bodyToMono(KakaoUserResponse.class)
                .block();

        return new KakaoOAuth2UserInfo(kakaoProfile);
    }
}
