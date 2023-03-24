package com.ecloth.beta.follow.dto;

import com.ecloth.beta.follow.entity.Follow;
import com.ecloth.beta.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class FollowingRequest {

    private Long targetId;
    @Builder.Default
    private boolean followStatus = true;
    public Follow toEntity(Member requester, Member target) {
        return Follow.builder()
                .requester(requester)
                .target(target)
                .build();
    }

}
