package com.ecloth.beta.post.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReplyResponse {
        private Long replyId;
        private Long commentId;
        private Long memberId;
        private String nickname;
        private String profileImagePath;
        private String content;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
}

