package com.ecloth.beta.follow.dto;

import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.follow.entity.Follow;
import com.ecloth.beta.follow.type.PointDirection;
import com.ecloth.beta.member.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FollowListResponse implements Serializable{

    private static final long serialVersionUID = 1L;
    private String pointDirection;
    private long total;
    @Builder.Default
    private CustomPage page = new CustomPage(1, 5, "createdDate", "dsc");
    private List<MemberShortInfo> followList;

    @AllArgsConstructor
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MemberShortInfo implements Serializable{

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

    public static FollowListResponse fromEmpty(PointDirection dir, CustomPage requestPage) {
        return FollowListResponse.builder()
                .total(0)
                .page(CustomPage.of(requestPage, 0))
                .pointDirection(dir.name().toUpperCase(Locale.ROOT))
                .followList(new ArrayList<>())
                .build();
    }

    public static FollowListResponse fromEntity(PointDirection dir, CustomPage requestPage,
                                                List<MemberShortInfo> followList) {

        CustomPage resultPage = CustomPage.of(requestPage, followList.size());

        return FollowListResponse.builder()
                .total(followList.size())
                .page(resultPage)
                .pointDirection(dir.name().toUpperCase(Locale.ROOT))
                .followList(followList)
                .build();
    }

}
