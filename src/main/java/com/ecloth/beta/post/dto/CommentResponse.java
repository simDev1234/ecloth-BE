package com.ecloth.beta.post.dto;

import lombok.Getter;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentResponse {
    private Long commentId;
    private Long postingId;
    private Long parentId;
    private Long memberId;
    private String nickname;
    private String profileImagePath;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}

