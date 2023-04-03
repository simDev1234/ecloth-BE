package com.ecloth.beta.domain.post.comment.service;

import com.ecloth.beta.domain.member.entity.Member;
import com.ecloth.beta.domain.member.repository.MemberRepository;
import com.ecloth.beta.domain.post.comment.dto.*;
import com.ecloth.beta.domain.post.comment.entity.Comment;
import com.ecloth.beta.domain.post.comment.exception.CommentException;
import com.ecloth.beta.domain.post.comment.repository.CommentRepository;
import com.ecloth.beta.domain.post.posting.entity.Posting;
import com.ecloth.beta.domain.post.posting.exception.PostingException;
import com.ecloth.beta.domain.post.posting.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ecloth.beta.domain.post.comment.exception.ErrorCode.COMMENT_NOT_FOUND;
import static com.ecloth.beta.domain.post.comment.exception.ErrorCode.NOT_COMMENT_WRITER;
import static com.ecloth.beta.domain.post.posting.exception.ErrorCode.POSTING_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;

    // 댓글 생성
    public CommentResponse createComment(CommentRequest request) {

        Posting posting = postingRepository.findById(request.getPostingId())
                .orElseThrow(() -> new PostingException(POSTING_NOT_FOUND));

        Member writer = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new UsernameNotFoundException("Member not Found"));

        Comment newComment = commentRepository.save(request.toComment(posting, writer));
        return CommentResponse.fromEntity(newComment, writer);
    }

    // 댓글 정보 조회 * 포스트의 댓글, 답글까지 모두 한 번에 조회, 답글 없는경우 댓글만 조회
    public CommentListResponse getCommentListByPostingId(Long postingId, CommentListRequest request) {

        Page<Comment> commentPage;

        if (request.getSortBy().equals("registerDate") && request.getSortOrder().equals("ASC")) {
            commentPage = commentRepository.findByPostingIdJoinedWithMember(postingId, request.toCustomPage().toPageable());
        } else {
            commentPage = commentRepository.findByPostingIdJoinedWithMemberAndReply(postingId, request.toCustomPage().toPageable());
        }

        List<CommentResponse> commentList = new ArrayList<>();
        for (Comment comment : commentPage) {
            CommentResponse response = CommentResponse.fromEntity(comment);
            if (comment.getReply() != null) {
                ReplyResponse replyResponse = ReplyResponse.fromEntity(comment.getReply());
                response.setReply(replyResponse);
            } else {
                response.setReply(null); // reply 필드가 null인 경우에는 null로 설정
            }
            commentList.add(response);
        }
        return CommentListResponse.from(commentList, (int) commentPage.getTotalElements(), request.toCustomPage());
    }

    // 댓글 수정
    public CommentResponse updateContent(Long commentId, CommentRequest commentRequest) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));

        // 댓글 작성자와 request 작성자 id 비교
        if (!comment.getWriter().getMemberId().equals(commentRequest.getMemberId())) {
            throw new CommentException(NOT_COMMENT_WRITER);
        }

        comment.updateContent(commentRequest.getContent());
        commentRepository.save(comment);

        return CommentResponse.fromEntity(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) {

        commentRepository.findById(commentId).ifPresent(commentRepository::delete);

    }

}
