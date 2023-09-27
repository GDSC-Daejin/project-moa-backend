package com.gdsc.moa.global.config;

import com.gdsc.moa.global.jwt.token.TokenProvider;
import com.gdsc.moa.global.jwt.exception.JwtAuthenticationEntryPoint;
import com.gdsc.moa.global.jwt.handler.JwtAccessDeniedHandler;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
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
            web.ignoring().requestMatchers(
                    new AntPathRequestMatcher("/swagger-ui/**"),
                    new AntPathRequestMatcher("/h2-console/**"),
                    new AntPathRequestMatcher("/favicon.ico"),
                    new AntPathRequestMatcher("/v2/api-docs"));
//                    TODO : h2 지우면 변경해야함
//                    "/swagger-ui/**", "/v2/api-docs"
//                    ,"/h2-console/**", "/favicon.ico");
        });
    }
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {

        http.cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            // exception handling 할 때 우리가 만든 클래스를 추가
            .exceptionHandling(exceptionHandling ->
                    exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                            .accessDeniedHandler(jwtAccessDeniedHandler))
            //세션 설정을 Stateless 로 설정
            .sessionManagement(sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            //권한 설정
            .authorizeHttpRequests(httpRequest ->
                    httpRequest.requestMatchers(new MvcRequestMatcher(introspector, "/**/user/**")).authenticated()
                        //TODO : 여기도 h2 지우면 변경해야함
//                            .requestMatchers(HttpMethod.DELETE, "/**/user").authenticated()
                            .anyRequest().permitAll())
            // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
            .apply(new JwtFilterConfig(tokenProvider));
        return http.build();
    }
}
