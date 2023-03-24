package com.ecloth.beta.post.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    private String content;

    private String nickname;

    private LocalDateTime date;

    private LocalDateTime replyTime; // 대댓글 작성 시간

    @OneToOne(mappedBy = "reply",fetch = FetchType.LAZY)

    private Comment comment;


//    public void setCommentId(Long commentId) {
//        this.commentId = commentId;
//    }

}

