package com.ecloth.beta.follow.dto;

import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.follow.entity.Follow;
import com.ecloth.beta.follow.type.PointDirection;
import com.ecloth.beta.member.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ecloth.beta.follow.type.PointDirection.FOLLOWS;

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

    public static FollowListResponse fromEntity(PointDirection dir, CustomPage page, Member member) {

        List<Follow> follows = FOLLOWS.equals(dir) ? member.getFollowList() : member.getFollowerList();

        int total = follows.size();
        int pageNumber = page.getPage();

        if (CollectionUtils.isEmpty(follows)) {
            return toFollowListResponse(member.getMemberId(), dir, pageNumber);
        }

        List<Follow> subFollowList = follows.subList(page.getStartIdx(), getEndIdx(page.getEndIdx(), total));
        return toFollowListResponse(member.getMemberId(), dir, total, pageNumber, subFollowList);
    }

    private static int getEndIdx(int endIdx, int total) {
        return endIdx > total - 1 ? total - 1 : endIdx;
    }

    private static FollowListResponse toFollowListResponse(Long memberId, PointDirection dir, int pageNumber) {
        return FollowListResponse.builder()
                .requesterId(memberId)
                .pointDirection(dir.name())
                .total(0)
                .page(CustomPage.builder()
                        .page(pageNumber)
                        .size(0)
                        .build())
                .followList(new ArrayList<>())
                .build();
    }

    private static FollowListResponse toFollowListResponse(Long memberId, PointDirection dir,
                                                           int total, int pageNumber,
                                                           List<Follow> subFollowList) {

        List<MemberShortInfo> memberShortInfos;

        if (FOLLOWS.equals(dir)) {
            memberShortInfos = subFollowList.stream()
                    .map(Follow::getTarget)
                    .map(MemberShortInfo::fromEntity).collect(Collectors.toList());
        } else {
            memberShortInfos = subFollowList.stream()
                    .map(Follow::getRequester)
                    .map(MemberShortInfo::fromEntity).collect(Collectors.toList());
        }

        return FollowListResponse.builder()
                .requesterId(memberId)
                .pointDirection(dir.name())
                .total(total)
                .page(CustomPage.builder()
                        .page(pageNumber)
                        .size(subFollowList.size())
                        .build())
                .followList(memberShortInfos)
                .build();
    }

}
