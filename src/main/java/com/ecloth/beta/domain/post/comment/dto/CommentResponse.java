package com.ecloth.beta.domain.post.comment.dto;

import com.ecloth.beta.domain.member.entity.Member;
import com.ecloth.beta.domain.post.comment.entity.Comment;
import com.ecloth.beta.domain.post.comment.entity.Reply;
import com.ecloth.beta.domain.post.posting.entity.Posting;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Builder
public class CommentResponse {

    private Long commentId;
    private Long writerId;
    private String nickname;
    private String profileImagePath;
    private String content;
    private ReplyResponse reply;
    private LocalDateTime registerDate;
    private LocalDateTime updatedDate;

    public static CommentResponse fromEntity(Comment comment, Member writer) {
        CommentResponse.CommentResponseBuilder builder = CommentResponse.builder()
                .commentId(comment.getCommentId())
                .writerId(writer.getMemberId())
                .nickname(writer.getNickname())
                .profileImagePath(writer.getProfileImagePath())
                .content(comment.getContent())
                .registerDate(comment.getRegisterDate())
                .updatedDate(comment.getUpdateDate());

        Reply reply = comment.getReply();
        if (reply != null) {
            builder.reply(ReplyResponse.fromEntity(reply));
        }

        return builder.build();
    }

    public static CommentResponse fromEntity(Comment comment) {
        CommentResponse.CommentResponseBuilder builder = CommentResponse.builder()
                .commentId(comment.getCommentId())
                .writerId(comment.getWriter().getMemberId())
                .nickname(comment.getWriter().getNickname())
                .profileImagePath(comment.getWriter().getProfileImagePath())
                .content(comment.getContent())
                .registerDate(comment.getRegisterDate())
                .updatedDate(comment.getUpdateDate());

        if (comment.getReply() != null) {
            builder.reply(ReplyResponse.fromEntity(comment.getReply()));
        } else {
            builder.reply(null); // reply 필드가 null인 경우에는 null로 설정
        }

        return builder.build();
    }
}

