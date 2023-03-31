package com.ecloth.beta.domain.post.comment.dto;

import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.domain.post.comment.entity.Reply;
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
@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReplyListResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private long total;
    @Builder.Default
    private CustomPage page = new CustomPage(1, 5, "registerDate", "ASC");
    private List<ReplyResponse> replyList;

    public static ReplyListResponse fromEntity(Page<Reply> replyPage) {

        CustomPage resultPage = CustomPage.of(replyPage.getPageable());

        return ReplyListResponse.builder()
                .total(replyPage.getTotalElements())
                .page(resultPage)
                .replyList(replyPage.getContent().stream()
                        .map(ReplyResponse::fromEntity)
                        .collect(Collectors.toList())
                )
                .build();
    }


}
