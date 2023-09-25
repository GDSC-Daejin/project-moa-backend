package com.example.moa.config;

import com.example.moa.api.jwt.token.TokenProvider;
import com.example.moa.api.jwt.exception.JwtAuthenticationEntryPoint;
import com.example.moa.api.jwt.handler.JwtAccessDeniedHandler;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> {
            web.ignoring()
                    .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                    .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**"))
                    .requestMatchers(new AntPathRequestMatcher("/v3/api-docs"))
                    .requestMatchers(new AntPathRequestMatcher("/favicon.ico"));
        });
    }
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                // TODO: 2023/09/25 cors는 어떻게 하냐...
                //.cors().and().csrf().disable()

                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .authorizeHttpRequests(a -> a
                        // TODO: 2023/09/25  제약 걸어줘야 하는데...
                        /// TODO: 2023/09/25 이거 에러난다..
                        //.requestMatchers(HttpMethod.DELETE, "/user/**").authenticated()
                        //.requestMatchers(PathRequest.toH2Console()).authenticated()
                        .anyRequest().permitAll())
                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .apply(new JwtFilterConfig(tokenProvider));
        return http.build();

    }


}
