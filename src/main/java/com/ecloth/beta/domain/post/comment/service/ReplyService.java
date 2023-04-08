package com.ecloth.beta.domain.post.comment.service;

import com.ecloth.beta.domain.member.entity.Member;
import com.ecloth.beta.domain.member.repository.MemberRepository;
import com.ecloth.beta.domain.post.comment.dto.ReplyRequest;
import com.ecloth.beta.domain.post.comment.dto.ReplyResponse;
import com.ecloth.beta.domain.post.comment.entity.Comment;
import com.ecloth.beta.domain.post.comment.entity.Reply;
import com.ecloth.beta.domain.post.comment.exception.CommentException;
import com.ecloth.beta.domain.post.comment.exception.ErrorCode;
import com.ecloth.beta.domain.post.comment.repository.CommentRepository;
import com.ecloth.beta.domain.post.comment.repository.ReplyRepository;
import com.ecloth.beta.domain.post.posting.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostingRepository postingRepository;

    // 대댓글 등록
    public ReplyResponse createReply(Long parentCommentId, ReplyRequest replyRequest) {

        Comment parentComment = commentRepository.findByCommentIdFetchJoinedWithPosting(parentCommentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        Member replyWriter = memberRepository.findById(replyRequest.getMemberId())
                .orElseThrow(() -> new CommentException(ErrorCode.REPLY_NOT_FOUND));
        log.info("대댓글 요청자 ID : "+ replyRequest.getMemberId());

        validateIfPostingWriterAndReplyWriterMatching(parentComment, replyWriter);

        Reply newReply = replyRepository.save(replyRequest.toEntity(parentComment, replyWriter));

        return ReplyResponse.fromEntity(newReply);
    }

    private void validateIfPostingWriterAndReplyWriterMatching(Comment parentComment, Member replyWriter) {

        Long postingId = parentComment.getPosting().getPostingId();
        Long replyWriterId = replyWriter.getMemberId();
        log.info("게시물ID : " +postingId + ",   대댓글작성요청자 : "+replyWriterId);

        boolean isPostingWriterAndReplyWriterMatching
                = postingRepository.isPostingWriterAndReplyWriterMatching(postingId, replyWriterId);

        log.info("게시물작성자와 대댓글요청자 일치여부 확인 : " + isPostingWriterAndReplyWriterMatching);

        if (!isPostingWriterAndReplyWriterMatching) {
            throw new CommentException(ErrorCode.POSTING_WRITER_REPLY_WRITER_NOT_MATCHING);
        }
    }


    // 대댓글 수정
    public ReplyResponse updateReply(Long replyId, ReplyRequest replyRequest) {

        Reply reply = replyRepository.findByReplyIdJoinedWithMember(replyId)
                .orElseThrow(() -> new RuntimeException("Reply not found"));

        validateIfRequesterMemberIsReplyWriter(replyRequest, reply);

        reply.updateContent(replyRequest.getContent());
        replyRepository.save(reply);

        return ReplyResponse.fromEntity(reply);
    }

    private static void validateIfRequesterMemberIsReplyWriter(ReplyRequest replyRequest, Reply reply) {

        Long replyWriterMemberId = reply.getWriter().getMemberId();

        if (!replyWriterMemberId.equals(replyRequest.getMemberId())) {
            throw new CommentException(ErrorCode.NOT_REPLY_WRITER);
        }

    }

    // 대댓글 삭제
    public void deleteReply(Long replyId) {
        replyRepository.findById(replyId).ifPresent(replyRepository::delete);
    }

}
