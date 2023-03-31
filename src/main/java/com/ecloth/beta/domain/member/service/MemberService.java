package com.ecloth.beta.domain.member.service;

import com.ecloth.beta.domain.member.dto.MemberInfoResponse;
import com.ecloth.beta.domain.member.dto.MemberLocationUpdateRequest;
import com.ecloth.beta.domain.member.dto.MemberUpdateInfoRequest;
import com.ecloth.beta.domain.member.entity.Member;
import com.ecloth.beta.domain.member.exception.MemberErrorCode;
import com.ecloth.beta.domain.member.exception.MemberException;
import com.ecloth.beta.domain.member.repository.MemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@Builder
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    // 회원 정보 조회
    public MemberInfoResponse getMemberInfo(Long memberId, String role) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_USER));

        // 프론트에서 OAuth 회원 여부에 대한 확인이 필요하여 아래와 같이 포맷을 맞춰 전달
        role = StringUtils.equals(role, "[ROLE_MEMBER]") ? "ROLE_MEMBER" : "ROLE_OAUTH_MEMBER";

        return MemberInfoResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .phone(member.getPhone())
                .profileImagePath(member.getProfileImagePath())
                .role(role)
                .build();
    }

    // 회원 정보 업데이트 : 마이페이지에서 수정하는 회원 정보
    public void updateMemberInfo(Long memberId, MemberUpdateInfoRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_USER));

        member.update(request, passwordEncoder);
        memberRepository.save(member);

    }

    // 회원 지역 업데이트
    public void updateMemberLocation(Long memberId, MemberLocationUpdateRequest request){

        Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        member.updateLocation(request.getX(), request.getY());
        memberRepository.save(member);
    }

    // 회원 ID 조회
    public Long getMemberId(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_USER));

        return member.getMemberId();
    }

    // 회원 탈퇴
    public void updateMemberStatus(Long memberId, String role) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_USER));

        member.updateMemberStatusToInactive();

        if (Objects.equals(role, "[ROLE_OAUTH_MEMBER]")) {

            log.info("MemberService.updateMemberStatus : OAuth 가입 회원의 OAuth, Server Refresh Token Redis에서 삭제");
            log.info(redisTemplate.opsForValue().get("KRT:"+ memberId));

            deleteOAuthRefreshTokenFromRedis(memberId, role);
            deleteServerRefreshTokenFromRedis(memberId);

        } else{

            log.info("MemberService.updateMemberStatus : 이메일 가입 회원의 Server Refresh Token Redis에서 삭제");
            log.info(redisTemplate.opsForValue().get("RT:"+memberId));

            deleteServerRefreshTokenFromRedis(memberId);
        }

        memberRepository.save(member);

    }

    private void deleteOAuthRefreshTokenFromRedis(Long memberId, String role) {
        redisTemplate.delete("KRT:" + memberId);
    }

    private void deleteServerRefreshTokenFromRedis(Long memberId){
        redisTemplate.delete("RT:" + memberId);
    }
}
