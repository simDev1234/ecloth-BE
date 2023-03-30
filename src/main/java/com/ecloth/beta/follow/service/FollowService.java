package com.ecloth.beta.follow.service;

import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.follow.dto.FollowListResponse;
import com.ecloth.beta.follow.dto.FollowListResponse.MemberShortInfo;
import com.ecloth.beta.follow.dto.FollowingResponse;
import com.ecloth.beta.follow.entity.Follow;
import com.ecloth.beta.follow.exception.FollowException;
import com.ecloth.beta.follow.repository.FollowRepository;
import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.ecloth.beta.follow.exception.ErrorCode.FOLLOW_REQUESTER_NOT_FOUND;
import static com.ecloth.beta.follow.type.PointDirection.FOLLOWERS;
import static com.ecloth.beta.follow.type.PointDirection.FOLLOWS;

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
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        Member target = memberRepository.findById(targetMemberId)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

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
    public FollowingResponse findMemberFollowInfo(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new FollowException(FOLLOW_REQUESTER_NOT_FOUND));

        return FollowingResponse.fromEntity(member, false);
    }

    // 팔로우 회원 목록
     public FollowListResponse findFollowMemberList(Long memberId, CustomPage requestPage) {

        List<Follow> followList = followRepository.findFollowListByRequesterId(memberId);

        if (CollectionUtils.isEmpty(followList)) {
            return FollowListResponse.fromEmpty(FOLLOWS, requestPage);
        }

        List<MemberShortInfo> subFollowList = getSubFollowListByPage(requestPage, followList);
        return FollowListResponse.fromEntity(FOLLOWS, requestPage, subFollowList);
    }

    private List<MemberShortInfo> getSubFollowListByPage(CustomPage requestPage, List<Follow> followList) {

        long total = followList.size();
        List<Follow> subFollowList = followList.subList(requestPage.findStartIdx(), requestPage.findEndIdx(total));

        return subFollowList.stream()
                .map(Follow::getTarget)
                .map(MemberShortInfo::fromEntity)
                .collect(Collectors.toList());
    }

    // 팔로워 회원 목록
    public FollowListResponse findFollowerMemberList(Long memberId, CustomPage requestPage) {

        List<Follow> followerList = followRepository.findFollowerListByTargetId(memberId);

        if (CollectionUtils.isEmpty(followerList)) {
            return FollowListResponse.fromEmpty(FOLLOWERS, requestPage);
        }

        List<MemberShortInfo> subFollowerList = getSubFollowerListByPage(requestPage, followerList);
        return FollowListResponse.fromEntity(FOLLOWERS, requestPage, subFollowerList);

    }

    private List<MemberShortInfo> getSubFollowerListByPage(CustomPage requestPage, List<Follow> followList) {

        long total = followList.size();
        List<Follow> subFollowList = followList.subList(requestPage.findStartIdx(), requestPage.findEndIdx(total));

        return subFollowList.stream()
                .map(Follow::getRequester)
                .map(MemberShortInfo::fromEntity)
                .collect(Collectors.toList());
    }

    // 언팔로우
    public void unfollow(Long requesterId, Long targetId) {
        followRepository.findByRequesterIdAndTargetId(requesterId, targetId)
                        .ifPresent(followRepository::delete);
    }

}