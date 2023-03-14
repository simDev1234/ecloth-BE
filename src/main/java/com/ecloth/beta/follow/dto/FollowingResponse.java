package com.ecloth.beta.follow.dto;

import com.ecloth.beta.member.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import java.io.Serializable;

@AllArgsConstructor
@Builder
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FollowingResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private FollowMemberInfo followMemberInfo;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class FollowMemberInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long targetId;
        private String nickname;
        private String profileImagePath;
        private long followCnt;
        private long followerCnt;
        private boolean followStatus;

        public static FollowMemberInfo fromEntity(Member target, boolean followStatus) {
            return FollowMemberInfo.builder()
                    .targetId(target.getMemberId())
                    .nickname(target.getNickname())
                    .profileImagePath(target.getProfileImagePath())
                    .followCnt(target.getFollowCnt())
                    .followerCnt(target.getFollowerCnt())
                    .followStatus(followStatus)
                    .build();
        }
    }

    public static FollowingResponse fromEntity(Member target, boolean followStatus){
        return new FollowingResponse(FollowMemberInfo.fromEntity(target, followStatus));
    }

}
