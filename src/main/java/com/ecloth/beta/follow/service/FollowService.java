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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.ecloth.beta.follow.exception.ErrorCode.FOLLOW_REQUESTER_NOT_FOUND;
import static com.ecloth.beta.follow.type.PointDirection.FOLLOWERS;
import static com.ecloth.beta.follow.type.PointDirection.FOLLOWS;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    // 팔로우
    public FollowingResponse follow(String currentLoggedInMemberEmail, Long toMemberId) {

        Member fromMember = memberRepository.findByEmail(currentLoggedInMemberEmail)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        Member toMember = memberRepository.findById(toMemberId)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        if (!isFollowing(currentLoggedInMemberEmail, toMemberId)) {
            followRepository.save(Follow.builder().requester(fromMember).target(toMember).build());
        }

        return FollowingResponse.fromEntity(toMember, true);
    }

    // 팔로우 상태 조회
    public boolean isFollowing(String currentLoggedInMemberEmail, Long toMemberId){

        Optional<Follow> optionalFollow
                = followRepository.findByRequesterEmailAndTargetId(currentLoggedInMemberEmail, toMemberId);

        return optionalFollow.isPresent();
    }

    // 회원의 닉네임, 프로필, 팔로우수, 팔로워수 정보 조회
    public FollowingResponse findMemberFollowInfo(String currentLoggedInMemberEmail) {

        Member member = memberRepository.findByEmail(currentLoggedInMemberEmail)
                .orElseThrow(() -> new FollowException(FOLLOW_REQUESTER_NOT_FOUND));

        return FollowingResponse.fromEntity(member, true);
    }

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

        Member member = memberRepository.findByEmail(currentLoggedInMemberEmail)
                .orElseThrow(() -> new FollowException(FOLLOW_REQUESTER_NOT_FOUND));

        List<MemberShortInfo> followList = findFollowingMemberShortList(requestPage, member);
        return FollowListResponse.fromEntity(FOLLOWS, requestPage, member, followList);
    }

    // 로그인 회원을 팔로우하고 있는 회원 목록
    public FollowListResponse findFollowerList(String currentLoggedInMemberEmail, CustomPage requestPage){

        Member member = memberRepository.findByEmail(currentLoggedInMemberEmail)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        List<MemberShortInfo> followerList = findFollowerMemberShortList(requestPage, member);
        return FollowListResponse.fromEntity(FOLLOWERS, requestPage, member, followerList);
    }

    // 특정 회원이 팔로우하고 있는 회원 목록
    public FollowListResponse findFollowList(Long memberId, CustomPage requestPage){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        List<MemberShortInfo> followList = findFollowingMemberShortList(requestPage, member);
        return FollowListResponse.fromEntity(FOLLOWERS, requestPage, member, followList);
    }

    // 특정 회원을 팔로우하고 있는 회원 목록
    public FollowListResponse findFollowerList(Long memberId, CustomPage requestPage){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        List<MemberShortInfo> followerList = findFollowerMemberShortList(requestPage, member);
        return FollowListResponse.fromEntity(FOLLOWERS, requestPage, member, followerList);
    }

    private List<MemberShortInfo> findFollowingMemberShortList(CustomPage requestPage, Member member) {

        List<Follow> myFollowList = member.getFollowList();
        List<Follow> subFollowList = getSubListByPage(requestPage, myFollowList);

        return subFollowList.stream()
                .map(Follow::getTarget)
                .map(MemberShortInfo::fromEntity)
                .collect(Collectors.toList());
    }

    private List<MemberShortInfo> findFollowerMemberShortList(CustomPage requestPage, Member member) {

        List<Follow> myFollowerList = member.getFollowerList();
        List<Follow> subFollowerList = getSubListByPage(requestPage, myFollowerList);

        return subFollowerList.stream()
                .map(Follow::getRequester)
                .map(MemberShortInfo::fromEntity)
                .collect(Collectors.toList());
    }

    private List<Follow> getSubListByPage(CustomPage requestPage, List<Follow> myFollowList) {

        long total = myFollowList.size();
        int endIdx = requestPage.getPage() * requestPage.getSize() - 1;
        int fromIdx = endIdx - requestPage.getSize() + 1;
        int toIdx = (int) Math.min(total, endIdx);

        return myFollowList.subList(fromIdx, toIdx);
    }

    // 언팔로우
    public void unfollow(String email, Long targetId) {
        Optional<Follow> optionalFollow = followRepository.findByRequesterEmailAndTargetId(email, targetId);

        if (optionalFollow.isPresent()) {
            followRepository.delete(optionalFollow.get());
        }
    }

}