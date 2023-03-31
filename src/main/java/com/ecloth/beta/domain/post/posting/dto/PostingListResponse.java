package com.ecloth.beta.domain.post.posting.dto;

import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.domain.post.posting.entity.Posting;
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
public class PostingListResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private long total;
    @Builder.Default
    private CustomPage page = new CustomPage(1, 10, "registerDate", "DESC");
    private List<PostingDetailResponse> postingList;

    public static PostingListResponse fromEntity(Page<Posting> postingPage){

        CustomPage resultPage = CustomPage.of(postingPage.getPageable());

        return PostingListResponse.builder()
                .total(postingPage.getTotalElements())
                .page(resultPage)
                .postingList(postingPage.getContent().stream()
                        .map(PostingDetailResponse::fromEntity).collect(Collectors.toList())
                )
                .build();
    }

}
