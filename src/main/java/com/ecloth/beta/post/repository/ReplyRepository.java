package com.ecloth.beta.post.repository;

import com.ecloth.beta.post.entity.Comment;
import com.ecloth.beta.post.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByParentComment(Comment parentComment);
}

