package com.ecloth.beta.post.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {

    private Long id;

    private String content;

    private String nickname;

    private LocalDateTime date;

    private int replyCount;

    private LocalDateTime commentTime;



}
