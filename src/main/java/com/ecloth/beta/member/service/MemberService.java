package com.ecloth.beta.member.service;

import com.ecloth.beta.member.component.JavaMailSenderComponent;
import com.ecloth.beta.member.dto.MemberRequest;
import com.ecloth.beta.member.dto.Token;
import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.exception.ErrorCode;
import com.ecloth.beta.member.exception.GlobalCustomException;
import com.ecloth.beta.member.jwt.JwtTokenProvider;
import com.ecloth.beta.member.model.MemberRole;
import com.ecloth.beta.member.model.MemberStatus;
import com.ecloth.beta.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
@Builder
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSenderComponent javaMailSenderComponent;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate redisTemplate;

    public Member register(MemberRequest.Register RegisterDto) {
        // 이메일 중복 체크
        if (memberRepository.existsByEmail(RegisterDto.getEmail())) {
            throw new GlobalCustomException(ErrorCode.ALREADY_USE_EMAIL);
        }
        // 닉네임 중복 체크
        if (memberRepository.existsByNickname(RegisterDto.getNickname())) {
            throw new GlobalCustomException(ErrorCode.ALREADY_USE_NICKNAME);
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
                .orElseThrow(() -> new GlobalCustomException(ErrorCode.INVALID_AUTH_CODE));

        // 이메일 인증 완료 후, 멤버 정보 업데이트
        member = member.toBuilder()
                .memberStatus(MemberStatus.ACTIVE)
                .emailAuthDate(LocalDateTime.now())
                .build();

        memberRepository.save(member);

    }

    @Transactional
    public Token login(MemberRequest.Login loginDto) {
        // 로그인 시 Email이 일치하면 유저 정보 가져오기
        Member member = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        // 로그인 시 패스워드가 불일치하면 에러 발생
        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        // AccessToken, Refresh Token 발급하기
        Token token = jwtTokenProvider.generateToken(member.getEmail());

        //redis에 RT:13@gmail.com(key) / 23jijiofj2io3hi32hiongiodsninioda(value) 형태로 리프레시 토큰 저장하기
        redisTemplate.opsForValue().set("RT:" + member.getEmail(), token.getRefreshToken(), token.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return token;
    }

    // 액세스 토큰 만료시 리프레시 토큰을 통한 재발급
    public ResponseEntity<Map<String, Object>> reissueToken(MemberRequest.Reissue reissueDto) {
        // RefreshToken 검증
        if (!jwtTokenProvider.validateToken(reissueDto.getRefreshToken())) {
            throw new GlobalCustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        // AccessToken 에서 email 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(reissueDto.getAccessToken());
        // Redis 에서 email 을 키값으로 저장된 RT 가져오기
        String refreshToken = (String) redisTemplate.opsForValue().get("RT:" + authentication.getName());
        // 로그아웃 되어 RefreshToken 이 삭제 되었을 경우
        if (ObjectUtils.isEmpty(refreshToken)) {
            throw new GlobalCustomException(ErrorCode.NOT_FOUND_REFRESH_TOKEN);
        }
        // 전달받은 RefreshToken과 Redis의 RefreshToken이 일치하지 않는 경우
        if (!refreshToken.equals(reissueDto.getRefreshToken())) {
            throw new GlobalCustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        // 새로운 토큰 생성
        Token token = jwtTokenProvider.generateToken(authentication.getName());
        // RefreshToken Redis에 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), token.getRefreshToken()
                        , token.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return ResponseEntity.ok()
                .header("Authorization", token.getAccessToken())
                .body(Map.of("message", "Token 정보가 갱신되었습니다."));
    }

    public ResponseEntity<String> logout(String accessToken) {
        // 로그아웃 하고 싶은 토큰이 유효한 지 먼저 검증하기
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException("로그아웃 : 유효하지 않은 토큰입니다.");
        }
        // Access Token에서 User email을 가져온다
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        // Redis에서 해당 User email로 저장된 Refresh Token 이 있는지 여부를 확인 후에 있을 경우 삭제를 한다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token을 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }

        // 해당 Access Token 유효시간을 가지고 와서 BlackList에 저장하기
        Long expiration = jwtTokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue().set("logout:" + accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        return ResponseEntity.ok()
                .body("로그아웃 되었습니다.");
    }

}
