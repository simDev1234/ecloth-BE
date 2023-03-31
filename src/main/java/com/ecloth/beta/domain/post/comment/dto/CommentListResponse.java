package com.ecloth.beta.domain.post.comment.dto;

import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.domain.post.comment.entity.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentListResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private long total;
    @Builder.Default
    private CustomPage page = new CustomPage(1, 10, "registerDate", "ASC");
    private List<CommentResponse> commentList;

    public static CommentListResponse fromEntity(Page<Comment> commentPage) {

        CustomPage resultPage = CustomPage.of(commentPage.getPageable());

        return CommentListResponse.builder()
                .total(commentPage.getTotalElements())
                .page(resultPage)
                .commentList(commentPage.getContent().stream()
                        .map(CommentResponse::fromEntity)
                        .collect(Collectors.toList())
                )
                .build();
    }

}
