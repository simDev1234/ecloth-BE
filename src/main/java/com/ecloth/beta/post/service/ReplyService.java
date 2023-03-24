package com.ecloth.beta.post.service;

import com.ecloth.beta.post.dto.ReplyRequest;
import com.ecloth.beta.post.dto.ReplyResponse;
import com.ecloth.beta.post.entity.Comment;
import com.ecloth.beta.post.entity.Reply;
import com.ecloth.beta.post.repository.CommentRepository;
import com.ecloth.beta.post.repository.ReplyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;

    public ReplyService(ReplyRepository replyRepository, CommentRepository commentRepository) {
        this.replyRepository = replyRepository;
        this.commentRepository = commentRepository;
    }

    // 대댓글 조회 Service 메서드
    public ReplyResponse getReplyById() throws Exception {
        Reply reply = replyRepository.findById(getReplyById().getId())
                .orElseThrow(() -> {
                    try {
                        return new Exception("Reply not found with id : " + getReplyById());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        return new ReplyResponse();
    }

    // 대댓글 작성 Service 메서드
    public Long createReply(ReplyRequest request) throws Exception {
        Comment comment = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new Exception("Comment not found with id : " + request.getCommentId()));
        Reply reply = Reply.builder()
                .content(request.getContent())
                .nickname(request.getNickname())
                .date(request.getDate())
                .replyTime(LocalDateTime.now())
                .comment(comment)
                .build();
        Reply savedReply = replyRepository.save(reply);
        return savedReply.getReplyId();
    }

    // 대댓글 수정 Service 메서드
    public void updateReply(ReplyRequest request) throws Exception {
        Reply reply = replyRepository.findById(request.getId())
                .orElseThrow(() -> new Exception("Reply not found with id : " + request.getId()));
        reply.setContent(request.getContent());
        reply.setReplyTime(request.getReplyTime());
        replyRepository.save(reply);
    }

    // 대댓글 삭제 Service 메서드
    public void deleteReply(Long replyId) throws Exception {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new Exception("Reply not found with id : " + replyId));
        replyRepository.delete(reply);
    }
}



