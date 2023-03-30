package com.ecloth.beta.post.dto;

import com.ecloth.beta.post.entity.Posting;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {
    @NotNull
    private Long memberId;
    @NotNull
    private Long postingId;
    private Long parentId;
    @NotBlank
    private String content;


}
