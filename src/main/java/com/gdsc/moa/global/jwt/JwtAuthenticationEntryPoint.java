package com.gdsc.moa.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.moa.global.dto.MoaApiResponse;
import com.gdsc.moa.global.message.DefaultMessage;
import com.gdsc.moa.global.message.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        // 401에러
        ResponseMessage errorMessage = DefaultMessage.UNAUTHORIZED;
        MoaApiResponse moaApiResponse = MoaApiResponse.createResponse(null, errorMessage);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(moaApiResponse));
    }
}
