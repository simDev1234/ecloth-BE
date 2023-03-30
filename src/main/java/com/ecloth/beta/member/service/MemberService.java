package com.ecloth.beta.member.service;

import com.ecloth.beta.member.dto.MemberGetInfoResponse;
import com.ecloth.beta.member.dto.MemberUpdateInfoRequest;
import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.exception.ErrorCode;
import com.ecloth.beta.member.exception.MemberException;
import com.ecloth.beta.member.repository.MemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final RedisTemplate<String, String> redisTemplate;

    // 회원 정보 조회
    public MemberGetInfoResponse getInfoMe(Long memberId, String role) {
        log.warn(role);
        if (Objects.equals(role, "[ROLE_MEMBER]")) {
            role = "ROLE_MEMBER";
        } else {
            role = "ROLE_OAUTH_MEMBER";
        }
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            throw new MemberException(ErrorCode.NOT_FOUND_USER);
        }
        return MemberGetInfoResponse.builder()
                .email(member.get().getEmail())
                .nickname(member.get().getNickname())
                .phone(member.get().getPhone())
                .profileImagePath(member.get().getProfileImagePath())
                .role(role)
                .build();
    }

    // 회원 정보 업데이트
    public void updateInfoMe(Long memberId, MemberUpdateInfoRequest request) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            member.update(request, passwordEncoder);
            memberRepository.save(member);
        }
    }

    // 회원 ID 조회
    public Long getMemberId(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        return member.getMemberId();

    }

    // 회원 탈퇴
    public void updateMemberStatus(Long memberId, String role) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            member.updateMemberStatusToInactive();
            memberRepository.save(member);

            if (Objects.equals(role, "[ROLE_OAUTH_MEMBER]")) {
                log.info(redisTemplate.opsForValue().get("KRT:"+memberId));
                redisTemplate.delete("KRT:" + memberId);
            }
            redisTemplate.delete("RT:" + memberId);
            log.info(redisTemplate.opsForValue().get("RT:"+memberId));

        }
    }
}
