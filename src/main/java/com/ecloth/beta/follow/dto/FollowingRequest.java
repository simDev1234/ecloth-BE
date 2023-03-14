package com.ecloth.beta.follow.dto;

import com.ecloth.beta.follow.entity.Follow;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.io.Serializable;

@AllArgsConstructor
@Builder
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FollowingRequest implements Serializable {

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
