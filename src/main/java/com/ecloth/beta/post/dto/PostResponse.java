package com.ecloth.beta.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String profileImage;
    private ArrayList<String> images;
    private String nickname;
    private LocalDateTime date;
    private int commentCount;
    private Long likeCount;
    private Long viewCount;
    private List<CommentResponse> comments;
}

