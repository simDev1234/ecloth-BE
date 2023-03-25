package com.ecloth.beta.post.controller;

import com.ecloth.beta.post.dto.ReplyRequest;
import com.ecloth.beta.post.dto.ReplyResponse;
import com.ecloth.beta.post.entity.Comment;
import com.ecloth.beta.post.entity.Reply;
import com.ecloth.beta.post.repository.ReplyRepository;
import com.ecloth.beta.post.service.ReplyService;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "대댓글API")
@RequestMapping("/comments/{commentId}/replies")
public class ReplyController {

    private final ReplyService replyService;
    private final ReplyRepository replyRepository;

    public ReplyController(ReplyService replyService,
                           ReplyRepository replyRepository) {
        this.replyService = replyService;
        this.replyRepository = replyRepository;
    }

    @PostMapping
    public ResponseEntity<ReplyResponse> createReply(@PathVariable Long commentId, @RequestBody ReplyRequest replyRequest,
                                                     @RequestHeader("X-MEMBER-ID") Long memberId) {
        ReplyResponse replyResponse = replyService.createReply(commentId, memberId, replyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(replyResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReplyResponse>> getReplies(@PathVariable Long commentId) {
        Comment parentComment = Comment.builder().commentId(commentId).build();
        List<Reply> replies = replyRepository.findByParentComment(parentComment);
        List<ReplyResponse> replyResponses = replies.stream().map(reply -> toReplyResponse(reply))
                .collect(Collectors.toList());
        return ResponseEntity.ok(replyResponses);
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
