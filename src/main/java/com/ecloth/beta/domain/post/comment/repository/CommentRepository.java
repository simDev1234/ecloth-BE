package com.ecloth.beta.domain.post.comment.repository;

import com.ecloth.beta.domain.post.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository{

    @Query("select c from Comment c join fetch c.posting where c.commentId = :commentId")
    Optional<Comment> findByCommentIdFetchJoinedWithPosting(Long commentId);

}
