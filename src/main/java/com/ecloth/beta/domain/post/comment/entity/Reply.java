package com.ecloth.beta.domain.post.comment.entity;

import com.ecloth.beta.common.entity.BaseEntity;
import com.ecloth.beta.domain.member.entity.Member;
import com.ecloth.beta.domain.post.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long replyId;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member writer;

    @OneToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    private String content;

    public void updateContent(String content) {
        this.content = content;
    }

}


