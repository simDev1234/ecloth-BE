package com.ecloth.beta.post.service;

import com.ecloth.beta.post.dto.CommentRequest;
import com.ecloth.beta.post.entity.Comment;
import com.ecloth.beta.post.entity.Reply;
import com.ecloth.beta.post.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Optional<Comment> createComment(CommentRequest comment) {
        comment.setDate(LocalDateTime.now());
        return commentRepository.findById(comment);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(new Comment());
    }

    public Comment updateComment(Long id, CommentRequest newComment) {
        Comment comment = commentRepository.findById(id).orElse(new Comment());
        if (comment == null) {
            return null;
        }

        comment = Comment.builder()
                .content(newComment.getContent())
                .nickname(newComment.getNickname())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .reply(new Reply())
                .build();
        return commentRepository.save(comment);

    }



    public boolean deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElse(new Comment());
        if (comment == null) {
            return false;
        }
        commentRepository.delete(comment);
        return true;
    }

    public Comment getComment(Long id) {
        return commentRepository.findById(id).orElse(new Comment());
    }
}

