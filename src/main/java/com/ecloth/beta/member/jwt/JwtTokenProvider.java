package com.ecloth.beta.member.jwt;

import com.ecloth.beta.member.dto.Token;
import com.ecloth.beta.member.security.MemberDetailService;
import com.ecloth.beta.member.security.MemberDetails;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.ecloth.beta.member.jwt.JwtProperties.*;
import static com.ecloth.beta.member.jwt.JwtProperties.BEARER_TYPE;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    private MemberDetails memberDetails;
    private final MemberDetailService memberDetailService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.token.key}")
    private String secretKey;

    // 토큰생성
    public Token generateToken(String email) {
        MemberDetails memberDetails = memberDetailService.loadUserByUsername(email);
        Date issuedAt = new Date();
        long now = new Date().getTime();

        Date accessTokenExpiredIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(email))
                .claim(AUTHORIZATION_HEADER, memberDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(issuedAt)
                .setExpiration(accessTokenExpiredIn)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();


        Date refreshTokenExpiredIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = Jwts.builder()
                .setSubject(email)
                .claim(AUTHORIZATION_HEADER, memberDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(issuedAt)
                .setExpiration(refreshTokenExpiredIn)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();


        return Token.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

    // 토큰에 들어있는 인증정보 가져오기
    public Authentication getAuthentication(String accessToken) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORIZATION_HEADER).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        memberDetails = memberDetailService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(memberDetails, "", authorities);
    }


    // 토큰 검증
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            log.error("JWT token is null or empty");
            return false;
        }
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature", e);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.", e);
        } catch (Exception e) {
            log.error("Exception occurred while parsing JWT token", e);
        }
        return false;
    }

    public Long getExpiration(String accessToken) {

        // AT 남은 유효시간
        Date expiration = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(accessToken)
                .getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    // Request Header 에서 JWT 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
