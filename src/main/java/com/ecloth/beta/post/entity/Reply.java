package com.ecloth.beta.post.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.ecloth.beta.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long replyId;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member replier;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    private String content;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public void update(String content) {
        this.content = content;
    }

    public boolean isOwnedBy(Member member) {
        return replier.equals(member);
    }

    public Comment getParentComment() {
        return parentComment;
    }
}


