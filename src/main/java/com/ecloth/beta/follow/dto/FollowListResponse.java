package com.ecloth.beta.follow.dto;

import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.follow.type.PointDirection;
import com.ecloth.beta.member.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

@AllArgsConstructor
@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FollowListResponse implements Serializable{

    private static final long serialVersionUID = 1L;
    private Long memberId;
    private String pointDirection;
    private long total;
    @Builder.Default
    private CustomPage page = new CustomPage(1, 5, "createdDate", "dsc");
    private List<MemberShortInfo> followList;

    @AllArgsConstructor
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MemberShortInfo {

        private Long targetId;
        private String nickname;
        private String profileImagePath;

        public static MemberShortInfo fromEntity(Member member) {
            return MemberShortInfo.builder()
                    .targetId(member.getMemberId())
                    .nickname(member.getNickname())
                    .profileImagePath(member.getProfileImagePath())
                    .build();
        }

    }

    public static FollowListResponse fromEntity(Long memberId, PointDirection dir,
                                                long total, CustomPage resultPage,
                                                List<MemberShortInfo> followList) {
        return FollowListResponse.builder()
                .memberId(memberId)
                .total(total)
                .page(resultPage)
                .pointDirection(dir.name().toUpperCase(Locale.ROOT))
                .followList(followList)
                .build();
    }

    public static FollowListResponse fromEntity(PointDirection dir, CustomPage requestPage,
                                                 Member member, List<MemberShortInfo> followList) {
        CustomPage resultPage = CustomPage.of(requestPage, followList.size());

        return FollowListResponse.fromEntity(
                member.getMemberId(),
                dir, followList.size(), resultPage,
                followList
        );
    }

}
