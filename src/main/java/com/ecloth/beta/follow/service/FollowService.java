package com.ecloth.beta.follow.service;

import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.follow.dto.FollowListResponse;
import com.ecloth.beta.follow.dto.FollowListResponse.MemberShortInfo;
import com.ecloth.beta.follow.dto.FollowingResponse;
import com.ecloth.beta.follow.entity.Follow;
import com.ecloth.beta.follow.exception.ErrorCode;
import com.ecloth.beta.follow.exception.FollowException;
import com.ecloth.beta.follow.repository.FollowRepository;
import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.ecloth.beta.follow.exception.ErrorCode.*;
import static com.ecloth.beta.follow.type.PointDirection.FOLLOWERS;
import static com.ecloth.beta.follow.type.PointDirection.FOLLOWS;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    public FollowingResponse follow(String requesterEmail, Long memberId) {

        Member requester = memberRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        Member target = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        Optional<Follow> optionalFollow = followRepository.findByRequesterAndTarget(requester, target);

        if (optionalFollow.isEmpty()) {
            followRepository.save(Follow.builder().requester(requester).target(target).build());
        }

        return FollowingResponse.fromEntity(target, true);
    }

    public boolean isFollowing(String requesterEmail, Long memberId){

        Member requester = memberRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        Member target = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        return followRepository.existsByRequesterAndTarget(requester, target);
    }

    public FollowingResponse findMemberFollowInfo(String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new FollowException(FOLLOW_REQUESTER_NOT_FOUND));

        return FollowingResponse.fromEntity(member, true);
    }

    public FollowingResponse findMemberFollowInfo(String email, Long memberId) {

        Member requester = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        Member target = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        boolean followStatus = followRepository.existsByRequesterAndTarget(requester, target);

        return FollowingResponse.fromEntity(target, followStatus);
    }

    public FollowListResponse findFollowList(String email, CustomPage requestPage) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        List<MemberShortInfo> followList = findFollowingMemberShortList(requestPage, member);
        return FollowListResponse.fromEntity(FOLLOWS, requestPage, member, followList);
    }

    public FollowListResponse findFollowList(Long memberId, CustomPage requestPage) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        List<MemberShortInfo> followList = findFollowingMemberShortList(requestPage, member);
        return FollowListResponse.fromEntity(FOLLOWS, requestPage, member, followList);
    }

    public FollowListResponse findFollowerList(String email, CustomPage requestPage){

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        List<MemberShortInfo> followerList = findFollowerMemberShortList(requestPage, member);
        return FollowListResponse.fromEntity(FOLLOWERS, requestPage, member, followerList);
    }

    public FollowListResponse findFollowerList(Long memberId, CustomPage requestPage){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        List<MemberShortInfo> followerList = findFollowerMemberShortList(requestPage, member);
        return FollowListResponse.fromEntity(FOLLOWERS, requestPage, member, followerList);
    }

    private List<MemberShortInfo> findFollowingMemberShortList(CustomPage requestPage, Member member) {

        List<Follow> myFollowList = member.getFollowList();
        List<Follow> subFollowList = getSubFollowListByPage(requestPage, myFollowList);

        return subFollowList.stream()
                .map(Follow::getTarget)
                .map(MemberShortInfo::fromEntity)
                .collect(Collectors.toList());
    }

    private List<MemberShortInfo> findFollowerMemberShortList(CustomPage requestPage, Member member) {

        List<Follow> myFollowerList = member.getFollowerList();
        List<Follow> subFollowerList = getSubFollowListByPage(requestPage, myFollowerList);

        return subFollowerList.stream()
                .map(Follow::getRequester)
                .map(MemberShortInfo::fromEntity)
                .collect(Collectors.toList());
    }

    private static List<Follow> getSubFollowListByPage(CustomPage requestPage, List<Follow> myFollowList) {
        long total = myFollowList.size();
        int fromIdx = requestPage.getStartIdx();
        int toIdx = (int) Math.min(total - 1, requestPage.getEndIdx());
        return myFollowList.subList(fromIdx, toIdx);
    }

    @Transactional
    public void unfollow(String email, Long targetId) {

        Member requester = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        Member target = memberRepository.findById(targetId)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        Follow follow = followRepository.findByRequesterAndTarget(requester, target)
                .orElseThrow(() -> new FollowException(ErrorCode.FOLLOW_NOT_FOUND));

        followRepository.delete(follow);

    }

}
