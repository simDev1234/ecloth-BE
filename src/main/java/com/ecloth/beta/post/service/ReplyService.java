package com.ecloth.beta.post.service;

import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.repository.MemberRepository;
import com.ecloth.beta.post.dto.ReplyRequest;
import com.ecloth.beta.post.dto.ReplyResponse;
import com.ecloth.beta.post.entity.Comment;
import com.ecloth.beta.post.entity.Reply;
import com.ecloth.beta.post.repository.CommentRepository;
import com.ecloth.beta.post.repository.ReplyRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
@Service
@Transactional
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public ReplyService(ReplyRepository replyRepository, CommentRepository commentRepository,
                        MemberRepository memberRepository) {
        this.replyRepository = replyRepository;
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
    }

    public ReplyResponse createReply(Long commentId, Long memberId, ReplyRequest replyRequest) {
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id " + commentId));
        Member replier = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id " + memberId));
        Reply reply = Reply.builder()
                .parentComment(parentComment)
                .replier(replier)
                .content(replyRequest.getContent())
                .build();
        Reply savedReply = replyRepository.save(reply);
        return toReplyResponse(savedReply);
    }

    private ReplyResponse toReplyResponse(Reply reply) {
        return ReplyResponse.builder()
                .replyId(reply.getReplyId())
                .commentId(reply.getParentComment().getCommentId())
                .memberId(reply.getReplier().getMemberId())
                .nickname(reply.getReplier().getNickname())
                .profileImagePath(reply.getReplier().getProfileImagePath())
                .content(reply.getContent())
                .createdDate(reply.getCreatedDate())
                .updatedDate(reply.getUpdatedDate())
                .build();
    }

}
