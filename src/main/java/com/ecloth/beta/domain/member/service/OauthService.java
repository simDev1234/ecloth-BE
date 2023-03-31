package com.ecloth.beta.domain.member.service;

import com.ecloth.beta.security.jwt.JwtTokenProvider;
import com.ecloth.beta.domain.member.dto.KakaoProfileRequest;
import com.ecloth.beta.domain.member.dto.OauthToken;
import com.ecloth.beta.domain.member.entity.Member;
import com.ecloth.beta.domain.member.model.MemberRole;
import com.ecloth.beta.domain.member.model.MemberStatus;
import com.ecloth.beta.domain.member.dto.Token;
import com.ecloth.beta.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final Map<String, OauthToken> oauthTokenMap = new ConcurrentHashMap<>();

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;


    // 카카오 서버에서 토큰 받아오기
    @Transactional
    public OauthToken getKakaoToken(String code) {
        log.info("authCode를 통해 getKakaoToken 메서드 들어옴");
        // HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret);

        // HttpHeader와 HttpBody 를 HttpEntity 객체에 담아 요청
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> KakaoTokenResponse = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // Http 응답, 응답값 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken;
        try {
            oauthToken = objectMapper.readValue(KakaoTokenResponse.getBody(), OauthToken.class);
            oauthTokenMap.put(oauthToken.getAccess_token(), oauthToken); //임시저장
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        log.info("해시맵 저장 : " + oauthTokenMap.get(oauthToken.getAccess_token()));
        log.info("카카오AT : " + oauthToken.getAccess_token());
        log.info("카카오RT : " + oauthToken.getRefresh_token());

        return oauthToken;

    }

    // 로그인 & JWT 토큰발급
    @Transactional
    public HttpHeaders kakaoRegisterAndGetToken(String token) {
        log.info("kakaoRegisterAndGetToken 들어옴");
        // 유저정보 받기
        KakaoProfileRequest userInfo = getUserInfo(token);
        // 최초 로그인시 회원정보 저장
        Member kakaoMember = memberRepository.findByEmail(userInfo.getKakao_account().getEmail())
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .email(userInfo.getKakao_account().getEmail())
                            .nickname(userInfo.getProperties().getNickname())
                            .profileImagePath(userInfo.getProperties().getProfile_image())
                            .memberRole(MemberRole.ROLE_OAUTH_MEMBER)
                            .memberStatus(MemberStatus.ACTIVE)
                            .build();

                    log.info("사이트 최초 로그인 회원정보저장");
                    return memberRepository.save(newMember);
                });
        // JWT 토큰 생성
        Token jwtDto = jwtTokenProvider.generateToken(kakaoMember.getMemberId());
        // Redis 에 JWT Refresh 토큰 저장
        redisTemplate.opsForValue()
                .set("RT:" + kakaoMember.getMemberId(),
                        jwtDto.getRefreshToken(), jwtDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        // Redis 에 Kakao Refresh 토큰 저장
        log.info("kakaoRegisterAndGetToken 에서 oauthTokenMap 을 이용해 Redis 저장위한 액토 리토 불러오기");

        // oauthTokenMap 에서 정보를 불러와 Redis 저장에 사용
        OauthToken oauthToken = oauthTokenMap.get(token);
        log.info("해시맵에서 불러온 주소 : " + oauthTokenMap.get(token));

        redisTemplate.opsForValue()
                .set("KRT:" + kakaoMember.getMemberId(),
                        oauthToken.getRefresh_token(), oauthToken.getRefresh_token_expires_in(), TimeUnit.SECONDS);
        oauthTokenMap.remove(token); //Redis에 저장 후 oauthTokenMap에 담긴 정보 삭제
        log.info("해시맵정보 레디스 저장후 삭제 확인 null : " + oauthTokenMap.get(token));

        // JWT AccessToken과 RefreshToken Http Header에 담아 반환하기
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + jwtDto.getAccessToken());
        headers.add("refreshtoken", "Bearer " + jwtDto.getRefreshToken());

        log.info("jwt AT : " + jwtDto.getAccessToken());
        log.info("jwt RT : " + jwtDto.getRefreshToken());
        log.info("kakao Redis RT : " + oauthToken.getRefresh_token());

        return headers;
    }

    // 유저 정보 가져오기
    @Transactional
    public KakaoProfileRequest getUserInfo(String token) {
        log.info("kakaoRegisterAndGetToken 에서 token을 통해 getUserInfo 메서드 들어옴");
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> kakaoUserResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // Http 응답, 유저 정보 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfileRequest kakaoUserInfo;
        try {
            kakaoUserInfo = objectMapper.readValue(kakaoUserResponse.getBody(), KakaoProfileRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("Kakao User Response Body: " + kakaoUserResponse.getBody());

        log.info("카카오 이메일 : " + kakaoUserInfo.getKakao_account().getEmail());
        log.info("카카오 닉네임 : " + kakaoUserInfo.getProperties().getNickname());
        log.info("카카오 프로필 : " + kakaoUserInfo.getProperties().getProfile_image());

        return kakaoUserInfo;
    }

    // 카카오 토큰 갱신
    @Transactional
    public void reissueKakaoToken(String redisKRT, String memberId) {
        // HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("refresh_token", redisKRT);
        body.add("client_secret", clientSecret);

        // HttpHeader와 HttpBody 를 HttpEntity 객체에 담아 요청
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> KakaoTokenResponse = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // Http 응답, 응답값 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken;
        try {
            oauthToken = objectMapper.readValue(KakaoTokenResponse.getBody(), OauthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Redis 에 새로운 카카오 RefreshToken 저장
        redisTemplate.opsForValue()
                .set("KRT:" + memberId, oauthToken.getRefresh_token()
                        , oauthToken.getRefresh_token_expires_in(), TimeUnit.SECONDS);
        log.info("카카오토큰갱신 AT : " + oauthToken.getAccess_token());
        log.info("카카오토큰갱신 RT : " + oauthToken.getRefresh_token());

    }


}
