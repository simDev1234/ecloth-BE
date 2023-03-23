package com.ecloth.beta.member.service;

import com.ecloth.beta.common.jwt.JwtTokenProvider;
import com.ecloth.beta.common.jwt.JwtTokenUtil;
import com.ecloth.beta.member.component.JavaMailSenderComponent;
import com.ecloth.beta.member.dto.InfoMeRequest;
import com.ecloth.beta.member.dto.MemberRequest;
import com.ecloth.beta.member.dto.Token;
import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.exception.ErrorCode;
import com.ecloth.beta.member.exception.MemberException;
import com.ecloth.beta.member.model.MemberRole;
import com.ecloth.beta.member.model.MemberStatus;
import com.ecloth.beta.member.repository.MemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@Transactional
@Builder
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSenderComponent javaMailSenderComponent;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final OauthService oauthService;

    @Transactional
    public Member register(MemberRequest.Register RegisterDto) {
        // 이메일 중복 체크
        if (memberRepository.existsByEmail(RegisterDto.getEmail())) {
            throw new MemberException(ErrorCode.ALREADY_EXIST_EMAIL);
        }
        // 닉네임 중복 체크
        if (memberRepository.existsByNickname(RegisterDto.getNickname())) {
            throw new MemberException(ErrorCode.ALREADY_EXIST_NICKNAME);
        }
        // 이메일 인증 코드 생성
        String emailAuthCode = UUID.randomUUID().toString().replace("-", "");

        Member member = Member.builder()
                .email(RegisterDto.getEmail())
                .password(passwordEncoder.encode(RegisterDto.getPassword()))
                .nickname(RegisterDto.getNickname())
                .phone(RegisterDto.getPhone())
                .memberRole(MemberRole.ROLE_MEMBER)
                .emailAuthCode(emailAuthCode)
                .memberStatus(MemberStatus.UNVERIFIED)
                .build();

        // 이메일 전송
        String subject = "이옷어때? 의 이메일 인증을 완료해주세요!";
        String content = "아래 링크를 클릭하여 이메일 인증을 진행해주세요. \n 이메일 인증 완료 후 모든 서비스를 이용 하실 수 있습니다. \n"
                + "http://localhost:8080/api/email-auth?code=" + emailAuthCode;

        javaMailSenderComponent.sendMail(RegisterDto.getEmail(), subject, content);

        // 회원 저장
        return memberRepository.save(member);

    }

    // 이메일 인증 완료 후, 멤버 정보를 업데이트하는 메소드
    public void updateMemberAfterEmailAuth(String emailAuthCode) {
        Member member = memberRepository.findByEmailAuthCode(emailAuthCode)
                .orElseThrow(() -> new MemberException(ErrorCode.INVALID_EMAIL_AUTH_CODE));

        // 이메일 인증 완료 후, 멤버 정보 업데이트
        member = member.toBuilder()
                .memberStatus(MemberStatus.ACTIVE)
                .emailAuthDate(LocalDateTime.now())
                .build();

        memberRepository.save(member);

    }

    @Transactional
    public HttpHeaders login(MemberRequest.Login loginDto) {
        // 이메일 검증
        Member member = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_USER));
        // 비밀번호 검증
        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new MemberException(ErrorCode.WRONG_PASSWORD);
        }
        // AccessToken, Refresh Token 생성하기
        Token token = jwtTokenProvider.generateToken(member.getEmail());

        // redis에 RT:이메일(key) / RT토큰(value) 형태로 리프레시 토큰 저장하기
        redisTemplate.opsForValue().set("RT:" + member.getEmail(), token.getRefreshToken(), token.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        // AccessToken과 RefreshToken Http Header에 담아 반환하기
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + token.getAccessToken());
        headers.add("refreshtoken", "Bearer " + token.getRefreshToken());
        return headers;
    }

    @Transactional
    public HttpHeaders reissueToken(String requestRT) {
        // "Bearer " 제거
        requestRT = requestRT.replace("Bearer ", "");
        // SecurityContext 에서 현재 인증 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // User email,role  가져오기
        String email = authentication.getName();
        String role = authentication.getAuthorities().toString();
        log.info("토큰갱신요청 사용자 이메일 : " + email);
        log.info("토큰갱신요청 사용자 역할 : " + role);

        HttpHeaders headers = new HttpHeaders();

        // 이메일 회원일경우
        if (role.equals("[[ROLE_MEMBER]]")) {
            log.info("ROLE_MEMBER 확인 토큰갱신 진행");
            // 새로운 토큰 생성
            Token token = jwtTokenProvider.generateToken(email);
            // RefreshToken Redis 에 업데이트
            redisTemplate.opsForValue()
                    .set("RT:" + email, token.getRefreshToken()
                            , token.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

            headers.add("authorization", "Bearer " + token.getAccessToken());
            headers.add("refreshtoken", "Bearer " + token.getRefreshToken());
            log.info("토큰갱신 AT : " + token.getAccessToken());
            log.info("토큰갱신 RT : " + token.getRefreshToken());
            return headers;

            // 카카오 회원일경우
        } else if (role.equals("[[ROLE_OAUTH_MEMBER]]")) {
            log.info("ROLE_OAUTH_MEMBER 확인 토큰갱신 진행");

            String redisKRT = redisTemplate.opsForValue().get("KRT:" + email);

            // Redis에서 저장된 kakao RefreshToken 값의 유효시간을 일 단위로 확인
            Long ttl = redisTemplate.getExpire("KRT:" + email, TimeUnit.DAYS);
            log.info("Redis key KRT:{} has TTL {} days", email, ttl);

            if (ttl != null && ttl < 30) {
                // 카카오 RefreshToken 유효기간이 30일 미만일경우 reissueKakaoToken 실행
                oauthService.reissueKakaoToken(redisKRT, email);
            }

            // 새로운 JWT토큰 생성
            Token token = jwtTokenProvider.generateToken(email);
            // RefreshToken Redis 에 업데이트
            redisTemplate.opsForValue()
                    .set("RT:" + email, token.getRefreshToken()
                            , token.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

            headers.add("authorization", "Bearer " + token.getAccessToken());
            headers.add("refreshtoken", "Bearer " + token.getRefreshToken());

            return headers;

        } else {
            throw new RuntimeException("유효하지 않은 회원 유형입니다.");
        }
    }

    public void logout(String accessToken) {
        // "Bearer " 제거
        accessToken = accessToken.replace("Bearer ", "");
        // SecurityContext 에서 현재 인증 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // User email 을 가져오기
        String email = authentication.getName();
        String role = authentication.getAuthorities().toString();

        // 이메일 회원일경우
        if (role.equals("[[ROLE_MEMBER]]")) {
            log.info("ROLE_MEMBER 확인 로그아웃 진행");

            // Redis 에서 해당 User email 로 저장된 Refresh Token 확인 후 있을 경우 삭제
            if (redisTemplate.opsForValue().get("RT:" + email) != null) {
                // 해당 Access Token 유효시간을 가지고 와서 BlackList 에 저장
                Long expiration = jwtTokenProvider.getExpiration(accessToken);
                jwtTokenUtil.setBlackListToken(email, accessToken, expiration);
                jwtTokenUtil.deleteRefreshToken(email);
            }
        } else if (role.equals("[[ROLE_OAUTH_MEMBER]]")) {
            log.info("ROLE_OAUTH_MEMBER 확인 로그아웃 진행");

            if (redisTemplate.opsForValue().get("KRT:" + email) != null &&
                    redisTemplate.opsForValue().get("RT:" + email) != null) {

                // 해당 JWT Access Token 유효시간을 가지고 와서 BlackList 에 저장
                Long expiration = jwtTokenProvider.getExpiration(accessToken);
                jwtTokenUtil.setBlackListToken(email, accessToken, expiration);
                jwtTokenUtil.deleteRefreshToken(email);

                // KRT 삭제
                jwtTokenUtil.deleteKakaoRefreshToken(email);

            }
        }
    }

    // 회원 정보 조회
    public InfoMeRequest getInfoMe() {
        // SecurityContext 에서 현재 인증 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // User email  가져오기
        String email = authentication.getName();
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()) {
            throw new MemberException(ErrorCode.NOT_FOUND_USER);
        }
        return InfoMeRequest.builder()
                .email(member.get().getEmail())
                .nickname(member.get().getNickname())
                .phone(member.get().getPhone())
                .profileImagePath(member.get().getProfileImagePath())
                .build();
    }

}
