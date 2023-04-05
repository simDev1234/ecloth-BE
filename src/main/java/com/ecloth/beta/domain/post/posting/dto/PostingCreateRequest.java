package com.ecloth.beta.domain.post.posting.dto;

import com.ecloth.beta.domain.member.entity.Member;
import com.ecloth.beta.domain.post.posting.entity.Posting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
public class PostingCreateRequest {

    private Long memberId;
    private String title;
    private String content;
    // private MultipartFile[] imageFiles;

    public Posting toPosting(Member writer){
        return Posting.builder()
                .writer(writer)
                .title(this.title)
                .content(this.content)
                .likeCount(0L)
                .viewCount(0L)
                .build();
    }

}