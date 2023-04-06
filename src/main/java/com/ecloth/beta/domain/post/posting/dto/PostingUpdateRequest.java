package com.ecloth.beta.domain.post.posting.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
public class PostingUpdateRequest {

    private String title;
    private String content;
    @ApiModelProperty(hidden = true)
    private Long memberId;
    private MultipartFile[] images;

}
