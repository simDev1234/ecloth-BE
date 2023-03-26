package com.ecloth.beta.post.service;

import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.repository.MemberRepository;
import com.ecloth.beta.post.dto.CommentRequest;
import com.ecloth.beta.post.dto.CommentResponse;
import com.ecloth.beta.post.entity.Comment;
import com.ecloth.beta.post.entity.Posting;
import com.ecloth.beta.post.repository.CommentRepository;
import com.ecloth.beta.post.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;

    public CommentResponse createComment(CommentRequest commentRequest) {
        Posting posting = postingRepository.findById(commentRequest.getPostingId())
                .orElseThrow(() -> new EntityNotFoundException("Posting not found with id " + commentRequest.getPostingId()));
        Member commenter = memberRepository.findById(commentRequest.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id " + commentRequest.getMemberId()));
        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .postingId(posting)
                .commenter(commenter)
                .build();
        Comment savedComment = commentRepository.save(comment);

        return toCommentResponse(savedComment);
    }


    public CommentResponse getComment(Long commentId) {
        Comment comment = getCommentById(commentId);
        return toCommentResponse(comment);
    }

    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest) {
        Comment comment = getCommentById(commentId);
        comment.update(commentRequest.getContent());
        return toCommentResponse(comment);
    }


    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow();
    }

    private CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .postingId(comment.getPostingId().getPostingId())
                .memberId(comment.getCommenter().getMemberId())
                .nickname(comment.getCommenter().getNickname())
                .profileImagePath(comment.getCommenter().getProfileImagePath())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .updatedDate(comment.getUpdatedDate())
                .build();
    }

}
