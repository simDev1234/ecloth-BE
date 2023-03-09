package com.ecloth.beta.follow.dto;

import com.ecloth.beta.follow.entity.Follow;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

public class Following {

    @AllArgsConstructor
    @Builder
    @Getter
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Request implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long targetId;
        @Builder.Default
        private boolean followStatus = true;
        public Follow toEntity(Long requesterId) {
            return Follow.builder()
                    .requesterId(requesterId)
                    .targetId(this.targetId)
                    .followStatus(this.followStatus)
                    .build();
        }
    }

    @AllArgsConstructor
    @Builder
    @Getter
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Response implements Serializable{

        private static final long serialVersionUID = 1L;
        private Long targetId;
        private boolean followStatus;

        public static Response fromEntity(Follow follow) {
            return Response.builder()
                    .targetId(follow.getTargetId())
                    .followStatus(follow.isFollowStatus())
                    .build();
        }
    }

}
