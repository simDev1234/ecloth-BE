package com.ecloth.beta.follow.service;

import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.follow.dto.FollowListResponse;
import com.ecloth.beta.follow.dto.FollowListResponse.MemberShortInfo;
import com.ecloth.beta.follow.dto.FollowingRequest;
import com.ecloth.beta.follow.dto.FollowingResponse;
import com.ecloth.beta.follow.entity.Follow;
import com.ecloth.beta.follow.exception.FollowException;
import com.ecloth.beta.follow.repository.FollowRepository;
import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static com.ecloth.beta.follow.exception.ErrorCode.*;
import static com.ecloth.beta.follow.type.PointDirection.FOLLOWERS;
import static com.ecloth.beta.follow.type.PointDirection.FOLLOWS;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    public FollowingResponse saveFollow(String requesterEmail, FollowingRequest request) {

        Member requester = memberRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new FollowException(FOLLOW_REQUESTER_NOT_FOUND));

        Member target = memberRepository.findById(request.getTargetId())
                .orElseThrow(() -> new FollowException(FOLLOW_TARGET_NOT_FOUND));

        boolean newFollowStatus = request.isFollowStatus();
        createOrUpdateFollow(request, requester);
        changeMemberFollowCount(requester, target, newFollowStatus);

        return FollowingResponse.fromEntity(target, newFollowStatus);
    }

    private void createOrUpdateFollow(FollowingRequest request, Member requester) {

        Optional<Follow> followHistory
                = followRepository.findByRequesterIdAndTargetId(requester.getMemberId(), request.getTargetId());

        Follow follow;
        if (followHistory.isPresent()) {
            follow = followHistory.get();
            validateIfAlreadyFollowOrUnFollow(request.isFollowStatus(), follow.isFollowStatus());
            follow.changeFollowStatus(request.isFollowStatus());
        } else {
            validateIfAlreadyFollowOrUnFollow(request.isFollowStatus(), false);
            follow = request.toEntity(requester.getMemberId());
        }

        followRepository.save(follow);
    }

    private static void changeMemberFollowCount(Member requester, Member target, boolean toBeFollowStatus) {
        requester.changeFollowCnt(toBeFollowStatus);
        target.changeFollowerCnt(toBeFollowStatus);
    }

    private static void validateIfAlreadyFollowOrUnFollow(boolean toBeFollowStatus, boolean asIsFollowStatus) {
        if (toBeFollowStatus == asIsFollowStatus) {
            log.info("FollowService.followOrUnFollowTarget : 팔로우(또는 언팔로우)를 이미 한 상태입니다.");
            throw new FollowException(FOLLOW_DUPLICATE_REQUEST);
        }
    }

    public FollowingResponse findFollowCountOfMine(String requesterEmail) {

        Member requester = memberRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new FollowException(FOLLOW_REQUESTER_NOT_FOUND));

        FollowingResponse response = FollowingResponse.fromEntity(requester, true);

        log.info("나 {}의 팔로우수 {}, 팔로워수 {}",
                requesterEmail, requester.getFollowCnt(), requester.getFollowerCnt());

        return response;
    }

    public FollowingResponse findFollowCountAndMyFollowingStatusOfTarget(String requesterEmail, long targetId) {

        Member requester = memberRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new FollowException(FOLLOW_REQUESTER_NOT_FOUND));

        Member target = memberRepository.findById(targetId)
                .orElseThrow(() -> new FollowException(FOLLOW_TARGET_NOT_FOUND));

        Optional<Follow> optionalFollow
                = followRepository.findByRequesterIdAndTargetId(requester.getMemberId(), targetId);

        if (optionalFollow.isEmpty()){
            log.info("{}의 팔로우 정보를 가져옵니다. (나의 팔로우 여부 : false - 팔로우 기록 없음)", target.getEmail());
            return FollowingResponse.fromEntity(target, false);
        }

        boolean isFollowStatus = optionalFollow.get().isFollowStatus();
        log.info("{}의 팔로우 정보를 가져옵니다. (나의 팔로우 여부 : {})", target.getEmail(), isFollowStatus);
        return FollowingResponse.fromEntity(target, isFollowStatus);
    }

    public FollowListResponse findFollowListOfMine(String requesterEmail, CustomPage requestPage) {

        Member requester = memberRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new FollowException(FOLLOW_REQUESTER_NOT_FOUND));

        return findFollowListOf(requester.getMemberId(), requestPage);
    }

    public FollowListResponse findFollowListOf(Long requesterId, CustomPage requestPage) {

        Page<Follow> pageResult = followRepository.findAll(requestPage.toPageRequest());
        List<MemberShortInfo> followList = addMemberShortInfoToFollowList(pageResult);
        CustomPage responsePage = CustomPage.of(requestPage, pageResult);
        return FollowListResponse.fromEntity(requesterId, FOLLOWS, pageResult.getTotalElements(), responsePage, followList);

    }

    public FollowListResponse findFollowerListOfMine(String requesterEmail, CustomPage requestPage) {

        Member requester = memberRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new FollowException(FOLLOW_REQUESTER_NOT_FOUND));

        return findFollowerListOf(requester.getMemberId(), requestPage);

    }

    public FollowListResponse findFollowerListOf(Long requesterId, CustomPage requestPage) {

        Page<Follow> pageResult = followRepository.findAllByTargetId(requesterId, requestPage.toPageRequest());
        List<MemberShortInfo> followerList = addMemberShortInfoToFollowList(pageResult);
        CustomPage responsePage = CustomPage.of(requestPage, pageResult);
        return FollowListResponse.fromEntity(requesterId, FOLLOWERS, pageResult.getTotalElements(), responsePage, followerList);

    }

    private List<MemberShortInfo> addMemberShortInfoToFollowList(Page<Follow> follows) throws RuntimeException{

        List<MemberShortInfo> followMembers = new ArrayList<>();

        for (Follow follow : follows.getContent()) {
            Optional<Member> optionalMember = memberRepository.findById(follow.getTargetId());

            if (optionalMember.isEmpty()) {
                log.warn("{} 가 Member 테이블에 존재하지 않습니다.", follow.getTargetId());
                // TODO 시큐러티 추가 후 UsernameNotFoundException으로 변경
                throw new RuntimeException("Member Not Found");
            }

            followMembers.add(MemberShortInfo.fromEntity(optionalMember.get()));
        }

        return followMembers;
    }

}
