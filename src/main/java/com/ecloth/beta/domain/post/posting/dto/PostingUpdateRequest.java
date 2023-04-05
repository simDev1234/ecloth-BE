package com.ecloth.beta.domain.post.posting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostingUpdateRequest {

    private String title;
    private String content;

}
