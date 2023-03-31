package com.ecloth.beta.security.jwt;

import com.ecloth.beta.domain.member.exception.MemberErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

//인증되지 않은 사용자가 권한이 필요한 자원에 접근하려고 할 때 Exception 발생
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException authException) throws IOException, ServletException {

        String exception = (String) request.getAttribute("exception");

        if (exception == null) {
            setResponse(response, MemberErrorCode.NOT_FOUND_TOKEN);
        } else if (exception.equals(MemberErrorCode.EXPIRED_TOKEN.name())) {
            setResponse(response, MemberErrorCode.EXPIRED_TOKEN);
        } else if (exception.equals(MemberErrorCode.INVALID_TOKEN.name())) {
            setResponse(response, MemberErrorCode.INVALID_TOKEN);
        } else if (exception.equals(MemberErrorCode.ALREADY_LOGOUT_TOKEN.name())) {
            setResponse(response, MemberErrorCode.ALREADY_LOGOUT_TOKEN);
        } else {
            setResponse(response, MemberErrorCode.NOT_FOUND_TOKEN);
        }

    }

    private void setResponse(HttpServletResponse response, MemberErrorCode errorCode)
            throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        String message = errorCode.getDetail();

        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorCode", errorCode.name());
        errorMap.put("message", message);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(errorMap);

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
        out.close();
    }

}


