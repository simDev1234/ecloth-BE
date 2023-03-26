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
    public FollowingResponse follow(String currentLoggedInMemberEmail, Long toMemberId) {

        Member requester = memberRepository.findByEmail(currentLoggedInMemberEmail)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        Member target = memberRepository.findById(toMemberId)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        if (!isFollowing(currentLoggedInMemberEmail, toMemberId)) {
            followRepository.save(Follow.builder().requester(requester).target(target).build());
        }

        return FollowingResponse.fromEntity(target, true);
    }

    // 팔로우 상태 조회
    public boolean isFollowing(String currentLoggedInMemberEmail, Long toMemberId) {

        Optional<Follow> optionalFollow
                = followRepository.findByRequesterEmailAndTargetId(currentLoggedInMemberEmail, toMemberId);

        return optionalFollow.isPresent();
    }

    // 로그인 회원의 닉네임, 프로필, 팔로우수, 팔로워수 정보 조회
    public FollowingResponse findMemberFollowInfo(String currentLoggedInMemberEmail) {

        Member member = memberRepository.findByEmail(currentLoggedInMemberEmail)
                .orElseThrow(() -> new FollowException(FOLLOW_REQUESTER_NOT_FOUND));

        return FollowingResponse.fromEntity(member, true);
    }

    // 특정 회원의 닉네임, 프로필, 팔로우수, 팔로워수 정보 조회
    public FollowingResponse findMemberFollowInfo(String currentLoggedInMemberEmail, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new FollowException(FOLLOW_REQUESTER_NOT_FOUND));

        if (isFollowing(currentLoggedInMemberEmail, memberId)) {
            return FollowingResponse.fromEntity(member, true);
        }

        return FollowingResponse.fromEntity(member, false);
    }

    // 로그인 회원이 팔로우하고 있는 회원 목록
    public FollowListResponse findFollowList(String currentLoggedInMemberEmail, CustomPage requestPage) {

        List<Follow> followList = followRepository.findFollowListByRequesterEmail(currentLoggedInMemberEmail);

        if (CollectionUtils.isEmpty(followList)) {
            return FollowListResponse.fromEmpty(FOLLOWS, requestPage);
        }

        List<MemberShortInfo> subFollowList = getSubFollowListByPage(requestPage, followList);
        return FollowListResponse.fromEntity(FOLLOWS, requestPage, subFollowList);
    }

    // 로그인 회원을 팔로우하고 있는 회원 목록
    public FollowListResponse findFollowerList(String currentLoggedInMemberEmail, CustomPage requestPage) {

        List<Follow> followerList = followRepository.findFollowerListByTargetEmail(currentLoggedInMemberEmail);

        if (CollectionUtils.isEmpty(followerList)) {
            return FollowListResponse.fromEmpty(FOLLOWERS, requestPage);
        }

        List<MemberShortInfo> subFollowerList = getSubFollowerListByPage(requestPage, followerList);
        return FollowListResponse.fromEntity(FOLLOWERS, requestPage, subFollowerList);
    }

    // 특정 회원이 팔로우하고 있는 회원 목록
     public FollowListResponse findFollowList(Long memberId, CustomPage requestPage) {

        List<Follow> followList = followRepository.findFollowListByRequesterId(memberId);

        if (CollectionUtils.isEmpty(followList)) {
            return FollowListResponse.fromEmpty(FOLLOWS, requestPage);
        }

        List<MemberShortInfo> subFollowList = getSubFollowListByPage(requestPage, followList);
        return FollowListResponse.fromEntity(FOLLOWS, requestPage, subFollowList);
    }

    // 특정 회원을 팔로우하고 있는 회원 목록
    public FollowListResponse findFollowerList(Long memberId, CustomPage requestPage) {

        List<Follow> followerList = followRepository.findFollowerListByTargetId(memberId);

        if (CollectionUtils.isEmpty(followerList)) {
            return FollowListResponse.fromEmpty(FOLLOWERS, requestPage);
        }

        List<MemberShortInfo> subFollowerList = getSubFollowerListByPage(requestPage, followerList);
        return FollowListResponse.fromEntity(FOLLOWERS, requestPage, subFollowerList);

    }

    private List<MemberShortInfo> getSubFollowListByPage(CustomPage requestPage, List<Follow> followList) {

        long total = followList.size();
        List<Follow> subFollowList = followList.subList(requestPage.getStartIdx(), requestPage.getEndIdx(total));

        return subFollowList.stream()
                .map(Follow::getTarget)
                .map(MemberShortInfo::fromEntity)
                .collect(Collectors.toList());
    }

    private List<MemberShortInfo> getSubFollowerListByPage(CustomPage requestPage, List<Follow> followList) {

        long total = followList.size();
        List<Follow> subFollowList = followList.subList(requestPage.getStartIdx(), requestPage.getEndIdx(total));

        return subFollowList.stream()
                .map(Follow::getRequester)
                .map(MemberShortInfo::fromEntity)
                .collect(Collectors.toList());
    }

    // 언팔로우
    public void unfollow(String email, Long targetId) {
        followRepository.findByRequesterEmailAndTargetId(email, targetId)
                        .ifPresent(followRepository::delete);
    }

}