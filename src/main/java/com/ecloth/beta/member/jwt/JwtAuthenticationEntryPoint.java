package com.ecloth.beta.member.jwt;

import com.ecloth.beta.member.exception.ErrorCode;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//인증되지 않은 사용자가 권한이 필요한 자원에 접근하려고 할 때 Exception 발생
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException authException) throws IOException, ServletException {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED); // 401


    }
//        String exception = (String) request.getAttribute("exception");
//
//        // 유효하지 않은 인증 정보 일경우
//        if(exception == null) {
//            setResponse(response, ErrorCode.INVALID_AUTHENTICATION);
//        }
//        // 액세스 토큰이 만료된 경우
//        else if(exception.equals(ErrorCode.EXPIRED_ACCESS_TOKEN.getCode())) {
//            setResponse(response, ErrorCode.EXPIRED_ACCESS_TOKEN);
//        }
//        // 유효하지 않은 토큰인 경우
//        else if(exception.equals(ErrorCode.INVALID_TOKEN.getCode())) {
//            setResponse(response, ErrorCode.INVALID_TOKEN);
//        }
//        else {
//            setResponse(response, ErrorCode.UNKNOWN_ERROR);
//        }
//    }
//
//    // 클라이언트에 JSON 형태의 응답 전송
//    private void setResponse(HttpServletResponse response,ErrorCode errorCode) throws IOException {
//        response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
//
//        JSONObject responseJson = new JSONObject();
//        responseJson.put("errorCode",errorCode.getCode());
//        responseJson.put("errorDetail",errorCode.getDetail());
//
//        response.getWriter().print(responseJson);
//    }
}
