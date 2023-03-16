package com.ecloth.beta.common.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//클라이언트에서 전달된 JWT 토큰을 검증하고, 검증된 정보를 기반으로 인증 객체(Authentication)를 생성하여 Spring Security 컨텍스트에 저장
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenUtil jwtTokenUtil;

    // 인증이나 권한이 필요한 주소 요청이 있을 때 해당 필터를 사용
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 헤더에서 JWT 를 받아온다
        String token = resolveToken((HttpServletRequest) request);
        // 요청 url 확인
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        log.info("JwtAuthenticationFilter : doFilter 들어옴");

        // 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 로그아웃된 토큰인지 검증
            if (!jwtTokenUtil.isTokenLoggedOut(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(),
                        requestURI);
            } else {
                log.warn("이미 로그아웃된 JWT 토큰입니다, uri: {}", requestURI);
            }
        } else {
            log.warn("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        chain.doFilter(request,response);

    }
    // Request Header 에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtProperties.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtProperties.BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
