package com.ecloth.beta.post.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentRequest {
    private String content;
    private String nickname;
    private LocalDateTime date;
    private LocalDateTime commentTime;

    public int getReplyCount() {
        return getReplyCount();
    }
}
