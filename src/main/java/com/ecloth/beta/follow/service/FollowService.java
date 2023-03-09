package com.ecloth.beta.follow.service;

import com.ecloth.beta.follow.dto.FollowList;
import com.ecloth.beta.follow.dto.FollowList.Response.FollowMember;
import com.ecloth.beta.follow.dto.Following;
import com.ecloth.beta.follow.entity.Follow;
import com.ecloth.beta.follow.entity.Member;
import com.ecloth.beta.follow.exception.FollowException;
import com.ecloth.beta.follow.repository.FollowRepository;
import com.ecloth.beta.follow.repository.MemberRepository;
import com.ecloth.beta.follow.type.PointDirection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.ecloth.beta.follow.exception.ErrorCode.FOLLOW_REQUESTER_NOT_FOUND;
import static com.ecloth.beta.follow.exception.ErrorCode.FOLLOW_TARGET_NOT_FOUND;
import static com.ecloth.beta.follow.type.PointDirection.FOLLOWS;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    public Following.Response followOrUnFollowTarget(String requesterEmail, Following.Request request) {

        Member requester = memberRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new FollowException(FOLLOW_REQUESTER_NOT_FOUND));

        Member target = memberRepository.findById(request.getTargetId())
                .orElseThrow(() -> new FollowException(FOLLOW_TARGET_NOT_FOUND));

        Optional<Follow> followHistory = followRepository.findByRequesterIdAndTargetId(requester.getId(), target.getId());

        if (followHistory.isPresent()) {
            Follow follow = followHistory.get();
            follow.changeFollowStatus(request.isFollowStatus());
            return Following.Response.fromEntity(followRepository.save(follow));
        }

        Follow newFollow = followRepository.save(request.toEntity(requester.getId()));

        return Following.Response.fromEntity(newFollow);
    }

    public Following.Response getFollowStatus(String requesterEmail, Long targetId) {

        Member requester = memberRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new FollowException(FOLLOW_REQUESTER_NOT_FOUND));

        Member target = memberRepository.findById(targetId)
                .orElseThrow(() -> new FollowException(FOLLOW_TARGET_NOT_FOUND));

        Optional<Follow> optionalFollow
                = followRepository.findByRequesterIdAndTargetId(requester.getId(), target.getId());

        if (optionalFollow.isEmpty()){
            return new Following.Response(targetId, false);
        }

        return Following.Response.fromEntity(optionalFollow.get());
    }

    public FollowList.Response getFollowList(String requesterEmail, FollowList.Request request) {

        Member requester = memberRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new FollowException(FOLLOW_REQUESTER_NOT_FOUND));

        PageRequest pageRequest = PageRequest.of(request.getPageNumber() - 1, request.getRecordSize()
                , Sort.Direction.valueOf(request.getSortOrder().toUpperCase(Locale.ROOT)), request.getSortBy());

        PointDirection pointDirection = PointDirection.valueOf(request.getPointDirection());

        Page<Follow> pageResult;
        List<FollowMember> followMembers;
        if (FOLLOWS.equals(pointDirection)) {
            pageResult = followRepository.findAll(pageRequest);
            followMembers = addMemberInfoToFollowResult(pageResult);
        } else  {
            pageResult = followRepository.findAllByTargetId(requester.getId(), pageRequest);
            followMembers = addMemberInfoToFollowResult(pageResult);
        }

        return FollowList.Response.fromEntity(requester.getId(), request, pageResult, followMembers);
    }

    private List<FollowMember> addMemberInfoToFollowResult(Page<Follow> follows) {

        List<FollowMember> followMembers = new ArrayList<>();

        for (Follow follow : follows.getContent()) {
            Member member = memberRepository.findById(follow.getTargetId())
                    .orElseThrow(() -> new FollowException(FOLLOW_TARGET_NOT_FOUND));

            FollowMember followMember = FollowMember.fromEntity(member);
            followMember.setFollowStatus(follow.isFollowStatus());
            followMembers.add(followMember);
        }

        return followMembers;
    }

}
