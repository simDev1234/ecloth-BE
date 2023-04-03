package com.ecloth.beta.domain.post.comment.dto;

import com.ecloth.beta.common.page.CustomPage;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentListResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private long total;
    @Builder.Default
    private CustomPage page = new CustomPage(1, 10, "registerDate", "ASC");
    @Builder.Default
    private List<CommentResponse> commentList = new ArrayList<>();

    private CommentListResponse(int total, CustomPage page, List<CommentResponse> commentList) {
        this.total = total;
        this.page = page;
        this.commentList = commentList;
    }

    public static CommentListResponse from(List<CommentResponse> commentList, int total, CustomPage page) {
        return new CommentListResponse(total, page, commentList != null ? commentList : Collections.emptyList());
    }

}
