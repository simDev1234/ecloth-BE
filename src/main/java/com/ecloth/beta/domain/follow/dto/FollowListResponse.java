package com.ecloth.beta.domain.follow.dto;

import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.domain.follow.entity.Follow;
import com.ecloth.beta.domain.follow.type.PointDirection;
import com.ecloth.beta.domain.member.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
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
    private CustomPage page = new CustomPage(1, 5, "registerDate", "DESC");
    private List<FollowMemberShortInfo> followList = new ArrayList<>();

    @AllArgsConstructor
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FollowMemberShortInfo implements Serializable{

        private Long targetId;
        private String nickname;
        private String profileImagePath;

        public static FollowMemberShortInfo fromEntity(Member member) {
            return FollowMemberShortInfo.builder()
                    .targetId(member.getMemberId())
                    .nickname(member.getNickname())
                    .profileImagePath(member.getProfileImagePath())
                    .build();
        }

    }

    public static FollowListResponse fromEntity(PointDirection dir, Page<Follow> followPage) {

        CustomPage resultPage = CustomPage.of(followPage.getPageable());

        return FollowListResponse.builder()
                .total(followPage.getTotalElements())
                .page(resultPage)
                .pointDirection(dir.name().toUpperCase(Locale.ROOT))
                .followList(followPage.getContent().stream()
                        .map(x -> x.getTarget())
                        .map(FollowMemberShortInfo::fromEntity)
                        .collect(Collectors.toList())
                )
                .build();
    }

}
