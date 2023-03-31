package com.ecloth.beta.domain.follow.dto;

import com.ecloth.beta.domain.member.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FollowingInfoResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long memberId;
    private String nickname;
    private String profileImagePath;
    private long followCnt;
    private long followerCnt;

    public static FollowingInfoResponse fromEntity(Member member) {
        return FollowingInfoResponse.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .profileImagePath(member.getProfileImagePath())
                .followCnt(member.getFollowList().size())
                .followerCnt(member.getFollowerList().size())
                .build();
    }



}
