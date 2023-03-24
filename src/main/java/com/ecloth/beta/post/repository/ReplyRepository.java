package com.ecloth.beta.post.repository;

import com.ecloth.beta.post.entity.Comment;
import com.ecloth.beta.post.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByCommentId(Long commentId);

    List<Reply> findByComment(Comment comment);

    Optional<Reply> findByReplyId(Long id);
}
