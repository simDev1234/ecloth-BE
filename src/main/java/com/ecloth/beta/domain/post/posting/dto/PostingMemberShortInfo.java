package com.ecloth.beta.domain.post.posting.dto;

import com.ecloth.beta.domain.member.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.io.Serializable;

@AllArgsConstructor
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostingMemberShortInfo implements Serializable {

    private Long memberId;
    private String nickname;
    private String profileImagePath;

    public static PostingMemberShortInfo fromEntity(Member member) {
        return PostingMemberShortInfo.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .profileImagePath(member.getProfileImagePath())
                .build();
    }

}
