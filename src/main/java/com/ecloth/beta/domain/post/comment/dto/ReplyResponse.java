package com.ecloth.beta.domain.post.comment.dto;

import com.ecloth.beta.domain.post.comment.entity.Reply;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReplyResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long replyId;
    private Long writerId;
    private String nickname;
    private String profileImagePath;
    private String content;
    private LocalDateTime registerDate;
    private LocalDateTime updatedDate;

    public static ReplyResponse fromEntity(Reply reply) {
        return ReplyResponse.builder()
                .replyId(reply.getReplyId())
                .writerId(reply.getWriter().getMemberId())
                .nickname(reply.getWriter().getNickname())
                .profileImagePath(reply.getWriter().getProfileImagePath())
                .content(reply.getContent())
                .registerDate(reply.getRegisterDate())
                .updatedDate(reply.getUpdateDate())
                .build();
    }

}

