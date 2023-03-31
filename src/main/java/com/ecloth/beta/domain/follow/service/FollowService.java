package com.ecloth.beta.domain.follow.service;

import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.domain.follow.dto.FollowListResponse;
import com.ecloth.beta.domain.follow.dto.FollowingInfoResponse;
import com.ecloth.beta.domain.follow.dto.FollowingResponse;
import com.ecloth.beta.domain.follow.entity.Follow;
import com.ecloth.beta.domain.follow.repository.FollowRepository;
import com.ecloth.beta.domain.member.entity.Member;
import com.ecloth.beta.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;
import static com.ecloth.beta.domain.follow.type.PointDirection.FOLLOWERS;
import static com.ecloth.beta.domain.follow.type.PointDirection.FOLLOWS;

/**
 * 팔로우 기능
 * - Requester : 팔로우를 요청한 회원
 * - Target : 팔로우 요청을 받은 회원
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    // 팔로우
    public FollowingResponse follow(Long requesterMemberId, Long targetMemberId) {

        Member requester = memberRepository.findById(requesterMemberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        Member target = memberRepository.findById(targetMemberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        if (!isFollowing(requesterMemberId, targetMemberId)) {
            followRepository.save(Follow.builder().requester(requester).target(target).build());
        }

        return FollowingResponse.fromEntity(target, true);
    }

    // 팔로우 상태 조회
    public boolean isFollowing(Long requesterMemberId, Long targetMemberId) {
        Optional<Follow> optionalFollow = followRepository.findByRequesterIdAndTargetId(requesterMemberId, targetMemberId);
        return optionalFollow.isPresent();
    }

    // 회원의 닉네임, 프로필, 팔로우수, 팔로워수 정보 조회
    public FollowingInfoResponse findMemberFollowInfo(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        return FollowingInfoResponse.fromEntity(member);
    }

    // 팔로우 회원 목록
     public FollowListResponse findFollowMemberList(Long memberId, CustomPage requestPage) {

         Page<Follow> followPage = followRepository.findFollowListByRequesterId(memberId, requestPage.toPageable());

         return FollowListResponse.fromEntity(FOLLOWS, followPage);
    }

    // 팔로워 회원 목록
    public FollowListResponse findFollowerMemberList(Long memberId, CustomPage requestPage) {

        Page<Follow> followerPage = followRepository.findFollowerListByTargetId(memberId, requestPage.toPageable());

        return FollowListResponse.fromEntity(FOLLOWERS, followerPage);

    }

    // 언팔로우
    public void unfollow(Long requesterId, Long targetId) {
        followRepository.findByRequesterIdAndTargetId(requesterId, targetId)
                        .ifPresent(followRepository::delete);
    }

}