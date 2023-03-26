package com.ecloth.beta.member.service;

import com.ecloth.beta.member.dto.InfoMeResponse;
import com.ecloth.beta.member.dto.InfoMeUpdateRequest;
import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.exception.ErrorCode;
import com.ecloth.beta.member.exception.MemberException;
import com.ecloth.beta.member.repository.MemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@Builder
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 정보 조회
    public InfoMeResponse getInfoMe(String memberId,String role) {
        log.warn(role);
        if (Objects.equals(role, "[ROLE_MEMBER]")){
            role = "ROLE_MEMBER";
        } else {
            role = "ROLE_OAUTH_MEMBER";
        }
        Optional<Member> member = memberRepository.findById(Long.valueOf(memberId));
        if (member.isEmpty()) {
            throw new MemberException(ErrorCode.NOT_FOUND_USER);
        }
        return InfoMeResponse.builder()
                .email(member.get().getEmail())
                .nickname(member.get().getNickname())
                .phone(member.get().getPhone())
                .profileImagePath(member.get().getProfileImagePath())
                .role(role)
                .build();
    }

    // 회원 정보 업데이트
    public void updateInfoMe(String memberId, InfoMeUpdateRequest request) {
        Optional<Member> memberOptional = memberRepository.findById(Long.valueOf(memberId));

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            member.update(request, passwordEncoder);
            memberRepository.save(member);
        }
    }
}
