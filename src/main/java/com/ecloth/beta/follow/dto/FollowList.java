package com.ecloth.beta.follow.dto;

import com.ecloth.beta.follow.entity.Follow;
import com.ecloth.beta.follow.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

public class FollowList {

    @AllArgsConstructor
    @Builder
    @Getter
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Request implements Serializable {

        private static final long serialVersionUID = 1L;

        @NotBlank
        private String pointDirection;
        @Builder.Default
        private int pageNumber = 1;
        @Builder.Default
        private int recordSize = 5;
        @Builder.Default
        private String sortBy = "createdDate";
        @Builder.Default
        private String sortOrder = "asc";

    }

    @AllArgsConstructor
    @Builder
    @Getter
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Response implements Serializable{

        private static final long serialVersionUID = 1L;

        private Long requesterId;
        private String pointDirection;
        private long total;
        private int pageNumber;
        private int recordSize;
        private String sortBy;
        private String sortOrder;
        private List<FollowMember> followList;

        public static Response fromEntity(Long requesterId, FollowList.Request request,
                                          Page<Follow> pageResult, List<FollowMember> followMembers) {
            return Response.builder()
                    .requesterId(requesterId)
                    .pointDirection(request.pointDirection)
                    .total(pageResult.getTotalElements())
                    .pageNumber(pageResult.getNumber())
                    .recordSize(pageResult.getNumberOfElements())
                    .sortBy(request.getSortBy())
                    .sortOrder(request.getSortBy())
                    .followList(followMembers)
                    .build();
        }

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        @Builder
        @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
        public static class FollowMember implements Serializable{

            private static final long serialVersionUID = 1L;

            private Long target_id;
            private String email;
            private String nickname;
            private String profileImagePath;
            private boolean followStatus;

            public static FollowMember fromEntity(Member member) {
                return FollowMember.builder()
                        .target_id(member.getId())
                        // TODO 추가
                        .build();
            }

        }

    }

}
