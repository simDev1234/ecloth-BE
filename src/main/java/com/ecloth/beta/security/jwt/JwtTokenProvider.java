package com.ecloth.beta.security.jwt;

import com.ecloth.beta.security.memberDetail.MemberDetailService;
import com.ecloth.beta.security.memberDetail.MemberDetails;
import com.ecloth.beta.domain.member.dto.Token;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final MemberDetailService memberDetailService;

    @Value("${jwt.token.key}")
    private String secretKey;

    // 토큰생성
    public Token generateToken(Long id) {
        MemberDetails memberDetails = memberDetailService.loadUserByUsername(String.valueOf(id));
        List<String> authorities = memberDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Date issuedAt = new Date();
        long now = new Date().getTime();

        Date accessTokenExpiredIn = new Date(now + JwtProperties.ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(memberDetails.getUsername())
                .claim("ROLE", authorities)
                .setIssuedAt(issuedAt)
                .setExpiration(accessTokenExpiredIn)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();


        Date refreshTokenExpiredIn = new Date(now + JwtProperties.REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = Jwts.builder()
                .setSubject(memberDetails.getUsername())
                .claim("ROLE", authorities)
                .setIssuedAt(issuedAt)
                .setExpiration(refreshTokenExpiredIn)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();


        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(JwtProperties.REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

    // JWT 토큰에서 인증정보 조회
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        // 사용자 정보 가져오기 (id,ROLE)
        MemberDetails memberDetails = memberDetailService.loadUserByUsername(claims.getSubject());
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("ROLE").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(memberDetails, "", authorities);


    }

    // JWT 토큰에서 클레임 추출
    public Claims parseClaims(String accessToken) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(accessToken)
                .getBody();
    }

    // JWT 토큰 유효시간 추출
    public Long getExpiration(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody().getExpiration();
        // 현재 시간
        long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            log.info("validate 접근");
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("토큰예외 : ExpiredJwtException");
            throw e;
        } catch (MalformedJwtException e) {
            log.warn("토큰예외 : MalformedJwtException");
            throw e;
        } catch (UnsupportedJwtException e) {
            log.warn("토큰예외 : UnsupportedJwtException");
            throw e;
        } catch (IllegalArgumentException e) {
            log.warn("토큰예외 : IllegalArgumentException");
            throw e;
        } catch (SignatureException e) {
            log.warn("토큰예외 : SignatureException");
            throw e;
        }
    }

}
