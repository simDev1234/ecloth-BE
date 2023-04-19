package com.ecloth.beta.security.jwt;

import com.ecloth.beta.utill.FilterResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        FilterResponseHandler.sendFilterExceptionResponse(response, "권한이 없습니다.", SC_FORBIDDEN);

    }

}
