package com.ecloth.beta.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    private String profileImage;
    private ArrayList<String> images;
    private String nickname;
    private LocalDateTime date;
    private int commentCount;
    private Long likeCount;
    private Long viewCount;
    private List<CommentRequest> comments;
}

