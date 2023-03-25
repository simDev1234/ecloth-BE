package com.ecloth.beta.post.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

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
public class Posting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postingId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member postingMember;

    private String nickname;
    private String profileImagePath;

    private String title;
    private String content;
    private String imagePath;

    private Long likeCount;
    private Long viewCount;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    private int commentCount;




    public void update(String title, String content, String imagePath) {
        this.title = title;
        this.content = content;
        this.imagePath = imagePath;
    }


}
