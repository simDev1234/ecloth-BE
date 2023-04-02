package com.ecloth.beta.domain.post.posting.dto;

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
    private MultipartFile[] images;

}
