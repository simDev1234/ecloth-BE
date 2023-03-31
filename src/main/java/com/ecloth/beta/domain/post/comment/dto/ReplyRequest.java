package com.ecloth.beta.domain.post.comment.dto;

import com.ecloth.beta.domain.member.entity.Member;
import com.ecloth.beta.domain.post.comment.entity.Comment;
import com.ecloth.beta.domain.post.comment.entity.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReplyRequest {

    private Long memberId;
    private String content;

    public Reply toEntity(Comment parentComment, Member writer){
        return Reply.builder()
                .writer(writer)
                .parentComment(parentComment)
                .content(this.content)
                .build();
    }
}

