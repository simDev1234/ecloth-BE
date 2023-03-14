package com.ecloth.beta.follow.dto;

import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.follow.type.PointDirection;
import com.ecloth.beta.member.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FollowListResponse implements Serializable{

    private static final long serialVersionUID = 1L;
    private Long requesterId;
    private String pointDirection;
    private long total;
    @Builder.Default
    private CustomPage page = new CustomPage(1, 5, "createdDate", "dsc");
    private List<MemberShortInfo> followList;

    @AllArgsConstructor
    @Getter
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class MemberShortInfo {

        private Long targetId;
        private String nickname;
        private String profileImagePath;

        public static MemberShortInfo fromEntity(Member member) {
            return new MemberShortInfo(
                    member.getMemberId(),
                    member.getNickname(),
                    member.getProfileImagePath()
            );
        }

    }

    public static FollowListResponse fromEntity(Long requesterId, PointDirection pointDirection, long total,
                                      CustomPage page, List<MemberShortInfo> followMemberInfos) {
        return FollowListResponse.builder()
                .requesterId(requesterId)
                .pointDirection(pointDirection.name())
                .total(total)
                .page(page)
                .followList(followMemberInfos)
                .build();
    }

}
