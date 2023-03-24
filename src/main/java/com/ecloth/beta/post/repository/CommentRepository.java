package com.ecloth.beta.post.repository;

import com.ecloth.beta.post.dto.CommentRequest;
import com.ecloth.beta.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {



    Optional<Comment> findById(CommentRequest comment);
}
