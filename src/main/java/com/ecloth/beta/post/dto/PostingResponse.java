package com.ecloth.beta.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostingResponse {

    private Long postId;
    private Long memberId;
    private String nickname;
    private String profileImagePath;
    private String title;
    private String content;
    private String imagePath;
    private Long likeCount;
    private Long viewCount;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private int commentCount;
}


