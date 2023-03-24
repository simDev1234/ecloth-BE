package com.ecloth.beta.post.dto;

import com.ecloth.beta.post.entity.Reply;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReplyResponse {

        private Long id;
        private String content;
        private String nickname;
        private LocalDateTime date;
        private LocalDateTime replyTime;


//        public ReplyResponse(Reply reply) {
//                this.id = reply.getCommentId(getId()));
//                this.content = reply.getContent();
//                this.nickname = reply.getNickname();
//                this.date = reply.getDate();
//                this.replyTime = reply.getReplyTime();
//        }


}
