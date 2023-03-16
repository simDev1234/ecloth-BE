package com.ecloth.beta.follow.service;

import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.follow.dto.FollowListResponse;
import com.ecloth.beta.follow.dto.FollowingResponse;
import com.ecloth.beta.follow.entity.Follow;
import com.ecloth.beta.follow.exception.ErrorCode;
import com.ecloth.beta.follow.exception.FollowException;
import com.ecloth.beta.follow.repository.FollowRepository;
import com.ecloth.beta.follow.type.PointDirection;
import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import static com.ecloth.beta.follow.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    public FollowingResponse createFollow(String requesterEmail, Long memberId) {

        Member requester = memberRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        Member target = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        Optional<Follow> optionalFollow = followRepository.findByRequesterAndTarget(requester, target);

        if (optionalFollow.isEmpty()) {
            createFollow(requester, target);
        }

        return FollowingResponse.fromEntity(target, true);
    }

    private void createFollow(Member requester, Member target) {
        followRepository.save(Follow.builder()
                .requester(requester)
                .target(target)
                .build());
    }

    public FollowingResponse findMyFollowDetail(String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new FollowException(FOLLOW_REQUESTER_NOT_FOUND));

        return FollowingResponse.fromEntity(member, true);
    }

    public FollowingResponse findFollowDetailOfMember(String email, Long memberId) {

        Member requester = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        Member target = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        boolean followStatus = followRepository.existsByRequesterAndTarget(requester, target);

        return FollowingResponse.fromEntity(target, followStatus);
    }

    public FollowListResponse findMyFollowList(String email, PointDirection dir, CustomPage requestPage) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        return FollowListResponse.fromEntity(dir, requestPage, member);
    }

    public FollowListResponse findFollowListOfMember(Long memberId, PointDirection dir, CustomPage requestPage) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        return FollowListResponse.fromEntity(dir, requestPage, member);

    }

    @Transactional
    public void stopFollowing(String email, Long targetId) {

        Member requester = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        Member target = memberRepository.findById(targetId)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        Follow follow = followRepository.findByRequesterAndTarget(requester, target)
                .orElseThrow(() -> new FollowException(ErrorCode.FOLLOW_NOT_FOUND));

        followRepository.delete(follow);

    }

}
