package com.ecloth.beta.follow.dto;

import com.ecloth.beta.member.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.io.Serializable;

@AllArgsConstructor
@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FollowingResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private MemberFollowInfo memberFollowInfo;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MemberFollowInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long targetId;
        private String nickname;
        private String profileImagePath;
        private long followCnt;
        private long followerCnt;
        private boolean followStatus;

        public static MemberFollowInfo fromEntity(Member member, boolean followStatus) {
            return MemberFollowInfo.builder()
                    .targetId(member.getMemberId())
                    .nickname(member.getNickname())
                    .profileImagePath(member.getProfileImagePath())
                    .followCnt(member.getFollowList().size())
                    .followerCnt(member.getFollowerList().size())
                    .followStatus(followStatus)
                    .build();
        }
    }

    public static FollowingResponse fromEntity(Member member, boolean followStatus){
        return new FollowingResponse(MemberFollowInfo.fromEntity(member, followStatus));
    }



}
