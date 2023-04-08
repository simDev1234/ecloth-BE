package com.ecloth.beta.domain.post.comment.repository;

import com.ecloth.beta.domain.post.comment.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("select r from Reply r join fetch r.writer where r.replyId = :replyId")
    Optional<Reply> findByReplyIdJoinedWithMember(Long replyId);

}

