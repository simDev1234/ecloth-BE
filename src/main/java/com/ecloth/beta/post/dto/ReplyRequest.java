package com.ecloth.beta.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyRequest {
    private Long commentId;
    private Long memberId;
    private String content;
}

