package com.ecloth.beta.common.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class JwtTokenUtil {
    private final RedisTemplate<String, String> redisTemplate;;

    // 로그아웃시 JWT RefreshToken 삭제
    public void deleteRefreshToken(String email) {
        redisTemplate.delete("RT:" + email);
    }

    // 로그아웃시 kakao RefreshToken 삭제
    public void deleteKakaoRefreshToken(String email){
        redisTemplate.delete("KRT:" + email);
    }

    // 삭제된 토큰 Blacklist 처리
    public void setBlackListToken(String email, String accessToken, long expiration) {
        // AccessToken 의 시간이 남아있다면 저장
        if (expiration > 0) {
            redisTemplate.opsForValue()
                    .set("logout:" + accessToken, email, Duration.ofMillis(expiration));
        }
    }

    public boolean isTokenLoggedOut(String token) {
        String logoutKey = "logout:" + token;
        Boolean isLoggedOut = redisTemplate.hasKey(logoutKey);
        return isLoggedOut != null && isLoggedOut;
    }

}
