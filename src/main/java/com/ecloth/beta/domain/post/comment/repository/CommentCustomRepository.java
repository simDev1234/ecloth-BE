package com.ecloth.beta.domain.post.comment.repository;

import com.ecloth.beta.domain.post.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentCustomRepository {
    Page<Comment> findByPostingIdJoinedWithMemberAndReply(Long postingId, Pageable pageable);
    Page<Comment> findByPostingIdJoinedWithMember(Long postingId, Pageable pageable);

}
