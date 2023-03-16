package com.ecloth.beta.member.jwt;

import com.ecloth.beta.member.dto.Token;
import com.ecloth.beta.member.security.MemberDetailService;
import com.ecloth.beta.member.security.MemberDetails;
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

import static com.ecloth.beta.member.jwt.JwtProperties.ACCESS_TOKEN_EXPIRE_TIME;
import static com.ecloth.beta.member.jwt.JwtProperties.REFRESH_TOKEN_EXPIRE_TIME;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final MemberDetailService memberDetailService;

    @Value("${jwt.token.key}")
    private String secretKey;

    // 토큰생성
    public Token generateToken(String email) {
        MemberDetails memberDetails = memberDetailService.loadUserByUsername(email);
        List<String> authorities = memberDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Date issuedAt = new Date();
        long now = new Date().getTime();

        Date accessTokenExpiredIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(memberDetails.getUsername())
                .claim("ROLE", authorities)
                .setIssuedAt(issuedAt)
                .setExpiration(accessTokenExpiredIn)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();


        Date refreshTokenExpiredIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
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
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

    // JWT 토큰에서 인증정보 조회
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        // 사용자 정보 가져오기 (이메일,ROLE)
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

    // JWT 토큰에서 이메일 추출
    public String getEmail(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody().getSubject();
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
        } catch (MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

}
