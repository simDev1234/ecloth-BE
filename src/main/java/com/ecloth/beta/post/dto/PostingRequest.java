package com.ecloth.beta.post.dto;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder(toBuilder = true)
public class PostingRequest {
    private Long memberId;
    private String title;
    private String content;
    private String imagePath;
}


