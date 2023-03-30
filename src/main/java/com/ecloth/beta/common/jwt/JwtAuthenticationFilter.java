package com.ecloth.beta.common.jwt;

import com.ecloth.beta.member.exception.ErrorCode;
import com.ecloth.beta.member.exception.MemberException;
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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

import static com.ecloth.beta.common.security.SecurityConfig.PERMIT_API_URL_ARRAY;
import static com.ecloth.beta.common.security.SecurityConfig.PERMIT_URL_ARRAY;

//클라이언트에서 전달된 JWT 토큰을 검증하고, 검증된 정보를 기반으로 인증 객체(Authentication)를 생성하여 Spring Security 컨텍스트에 저장
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //TODO : 예외처리 필요

        // 요청 url 확인
        String requestURI = ((HttpServletRequest) request).getRequestURI();

        // 요청 url이 허용된 url 리스트에 포함되어 있는지 확인
        if (Arrays.asList(PERMIT_API_URL_ARRAY).contains(requestURI) || Arrays.asList(PERMIT_URL_ARRAY).contains(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        // 헤더에서 JWT 를 받아온다
        String token = resolveToken((HttpServletRequest) request);
        log.info("JwtAuthenticationFilter : doFilter 들어옴");
        log.info("입력 토큰 : " + token);

        // 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 로그아웃된 토큰인지 검증
            if (!jwtTokenUtil.isTokenLoggedOut(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            } else {
                log.warn("이미 로그아웃된 JWT 토큰입니다, uri: {}", requestURI);
                throw new MemberException(ErrorCode.ALREADY_LOGOUT_TOKEN);
            }
        } else {
            log.warn("JWT 토큰이 없습니다, uri: {}", requestURI);
            throw new MemberException(ErrorCode.NOT_FOUND_TOKEN);
        }

        chain.doFilter(request, response);

    }

    // Request Header 에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtProperties.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtProperties.BEARER_TYPE)) {
            log.info("Bearer 토큰 추출 : " + bearerToken.substring(7));
            return bearerToken.substring(7);
        }

        //요청 헤더에서 JWT 토큰을 찾을 수 없을 때, 쿠키에서 refreshtoken 값을 추출하여 반환.
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshtoken")) {
                    log.info("쿠키에서 리프레시토큰 추출 : " + cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
