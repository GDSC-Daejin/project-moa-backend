package com.gdsc.moa.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.moa.global.dto.MoaApiResponse;
import com.gdsc.moa.global.message.DefaultMessage;
import com.gdsc.moa.global.message.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();

        // 403에러
        ResponseMessage errorMessage = DefaultMessage.FORBIDDEN;
        MoaApiResponse moaApiResponse = MoaApiResponse.createResponse(null, errorMessage);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorMessage.getStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(moaApiResponse));
    }
}
