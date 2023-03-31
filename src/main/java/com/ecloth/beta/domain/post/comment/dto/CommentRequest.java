package com.ecloth.beta.domain.post.comment.dto;

import com.ecloth.beta.domain.member.entity.Member;
import com.ecloth.beta.domain.post.comment.entity.Comment;
import com.ecloth.beta.domain.post.posting.entity.Posting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {
    @NotNull
    private Long memberId;
    private Long postingId;
    @NotBlank
    private String content;

    public Comment toComment(Posting posting, Member writer){
        return Comment.builder()
                .posting(posting)
                .writer(writer)
                .content(this.content)
                .build();
    }

}
