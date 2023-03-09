package com.ecloth.beta.member.service;

import com.ecloth.beta.member.component.JavaMailSenderComponent;
import com.ecloth.beta.member.dto.MemberRegisterDto;
import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.exception.ErrorCode;
import com.ecloth.beta.member.exception.GlobalCustomException;
import com.ecloth.beta.member.model.MemberRole;
import com.ecloth.beta.member.model.MemberStatus;
import com.ecloth.beta.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
@Builder
public class MemberRegisterService {

    private MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSenderComponent javaMailSenderComponent;

    public Member register(MemberRegisterDto RegisterDto) {
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
                .memberRole(MemberRole.EMAIL_MEMBER)
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

}
