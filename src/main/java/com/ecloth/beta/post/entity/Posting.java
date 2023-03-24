package com.ecloth.beta.post.entity;

import com.ecloth.beta.post.dto.PostRequest;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Table(name = "POST")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Posting {
    private static java.time.LocalDateTime LocalDateTime;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String title;
    private String content;

//    private ArrayList<String> images;
    private String nickname;
    @CreatedDate
    private LocalDateTime createdAt;
    private Long likeCount;
    private Long viewCount;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;


    public static Posting from(PostRequest request) {
        return Posting.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .nickname(request.getNickname())
                .likeCount(0L)
                .viewCount(0L)
                .build();
    }
}




