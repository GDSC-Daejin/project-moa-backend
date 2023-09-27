package com.gdsc.moa.user.service;

import com.gdsc.moa.api.controller.KakaoOAuth2;
import com.gdsc.moa.global.jwt.dto.KakaoUserInfo;
import com.gdsc.moa.global.jwt.dto.KakaoUserResponse;
import com.gdsc.moa.global.jwt.dto.OauthToken;
import com.gdsc.moa.global.jwt.dto.TokenResponse;
import com.gdsc.moa.global.jwt.token.TokenProvider;
import com.gdsc.moa.user.entity.RoleType;
import com.gdsc.moa.user.entity.UserEntity;
import com.gdsc.moa.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final KakaoOAuth2 kakaoOAuth2;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;




    public OauthToken getKakaoOauthToken(String authorizedCode)  {
        log.info("getAccessToken 호출");
        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "fd579855697f875ec02960113d0f8f87");
        params.add("redirect_uri", "http://localhost:3000/oauth/kakao/redirect");
        params.add("client_secret", "U0AVjx5zSmiTQpQZROrjAE0rXojPmIKi");
        params.add("code", authorizedCode);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        log.info("카카오 토큰 요청");
        // Http 요청하기, Post방식으로, 그리고 response 변수의 응답 받음
        /// TODO: 2023/09/26 여기가 문제다 시벌
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        log.info("response: {}", response);
        log.info("카카오 토큰 요청 완료");
        // JSON -> 액세스 토큰 파싱
        String tokenJson = response.getBody();
        Gson gson = new Gson();
        OauthToken tokenResponse = gson.fromJson(tokenJson, OauthToken.class);

        return tokenResponse;
    }
    public TokenResponse SaveUserAndGetToken(String access_token) {
        KakaoUserInfo profile = getUserInfoByToken(access_token);

        UserEntity user = userRepository.findByEmail(profile.getEmail());
        if(user == null) {
            user = UserEntity.builder()
                    .kakaoEmail(profile.getEmail())
                    .kakaoNickname(profile.getNickname())
                    .kakaoProfileImg(profile.getProfileImageUrl())
                    .userRole(RoleType.MEMBER)
                    .build();

            userRepository.save(user);
        }

        return createToken(user);
    }

    private TokenResponse createToken(UserEntity user) {
        return tokenProvider.generateJwtToken(user.getEmail(), user.getNickname(), RoleType.MEMBER);

        // Jwt 생성 후 헤더에 추가해서 보내줌
        /*
        String jwtToken = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.EXPIRATION_TIME))
                .withClaim("id", user.getId())
                .withClaim("nickname", user.getNickname())
                .sign(Algorithm.HMAC512(JwtRequestFilter.S));
         */
    }

    private KakaoUserInfo getUserInfoByToken(String accessToken) {
        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        // Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        log.info("Kakao API Response: {}", kakaoProfileResponse);
        //Gson gson = new Gson();
        //KakaoUserResponse kakaoUserResponse = gson.fromJson(response.getBody(), KakaoUserResponse.class);
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoUserResponse kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoUserResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.info("Kakao API kakaoUserResponse: {}", kakaoProfile);

        //가져온 사용자 정보를 객체로 만들어서 반환
        return new KakaoUserInfo(kakaoProfile.getKakao_account().getEmail(), kakaoProfile.getKakao_account().getProfile().getNickname(), kakaoProfile.getProperties().getProfile_image());
    }

    /*
    public void getKakaoToken(String authorizedCode) {
        log.info("kakaoLogin Service 호출");
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
        System.out.println("userInfo.getNickname() = " + userInfo.getNickname());

        //카카오에서 받아온 사용자의 정보
        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
        String email = userInfo.getEmail();
        String profileImage = userInfo.getProfileImageUrl();

        //카카오 로그인을 통해 이미 회원가입한 회원인지 확인하기 위해 카카오ID를 통해 검색
        UserEntity kakaoUser = userRepository.findById(kakaoId)
                .orElse(null);

        //중복된 사용자가 없다면(처음으로 카카오 로그인을 하는 경우 카카오에서 받은 정보를 통한 회원가입 진행)
        if(kakaoUser == null) {
            UserEntity sameEmailMember = userRepository.findByEmail(email).orElse(null);
            if(sameEmailMember != null){
                //카카오로그인은 처음인데 이미 그냥 회원가입은 돼있는경우
                kakaoUser = sameEmailMember;
                kakaoUser.setId(kakaoId);    //이미 저장돼있는 회원 정보에 카카오 ID만 추가해서 다시 저장한다.
                userRepository.updateMember(kakaoUser);     //기존 회원에 카카오 아이디만 추가해준다
            }
            else{
                String userName = nickname;
                String password = kakaoId + ADMIN_TOKEN;
                String encodedPassword = passwordEncoder.encode(password);
                kakaoUser = UserEntity.builder()
                        .kakao_id(kakaoId)
                        .login_id(email)    //카카오 로그인의 경우에는 login id가 없기에 이메일을 넣어줌
                        .nickname(nickname)
                        .name(userName)
                        .email(email)
                        .login_password(encodedPassword)
                        .build();
                userRepository.save(kakaoUser); //회원가입

                int idByLoginId = userRepository.findIdByLoginId(email);
                kakaoUser.setMember_id(idByLoginId);
            }
        }

        // 로그인 처리
        // 스프링 시큐리티 통해 인증된 사용자로 등록
        MemberDetailsImpl memberDetails = new MemberDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    */
}
