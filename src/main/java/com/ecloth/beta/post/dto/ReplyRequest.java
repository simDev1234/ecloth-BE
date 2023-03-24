package com.ecloth.beta.post.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReplyRequest {

    private Long id;
    private String content;
    private String nickname;
    private Long commentId;
    private LocalDateTime date;
    private LocalDateTime replyTime;

}
