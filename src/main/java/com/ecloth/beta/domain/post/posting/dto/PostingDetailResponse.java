package com.ecloth.beta.domain.post.posting.dto;

import com.ecloth.beta.domain.post.comment.dto.CommentResponse;
import com.ecloth.beta.domain.post.comment.entity.Comment;
import com.ecloth.beta.domain.post.posting.entity.Image;
import com.ecloth.beta.domain.post.posting.entity.Posting;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostingDetailResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long postingId;
    private PostingMemberShortInfo member;
    private String title;
    private String content;
    private List<String> imagePaths;
    private Long likeCount; // 좋아요
    private Long viewCount; // 조회수
    private LocalDateTime registerDate;
    private LocalDateTime updatedDate;

    public static PostingDetailResponse fromEntity(Posting posting) {
        return PostingDetailResponse.builder()
                .postingId(posting.getPostingId())
                .member(PostingMemberShortInfo.fromEntity(posting.getWriter()))
                .title(posting.getTitle())
                .content(posting.getContent())
                .imagePaths(posting.getImageList().stream().map(Image::getUrl).collect(Collectors.toList()))
                .likeCount(posting.getLikeCount())
                .viewCount(posting.getViewCount())
                .registerDate(posting.getRegisterDate())
                .updatedDate(posting.getUpdateDate())
                .build();
    }

}
