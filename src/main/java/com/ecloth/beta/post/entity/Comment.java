package com.ecloth.beta.post.entity;


import com.ecloth.beta.member.entity.Member;
import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posting postingId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member commenter;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    public void update(String content) {
        this.content = content;
    }

    public boolean isOwnedBy(Member member) {
        return commenter.equals(member);
    }

}


