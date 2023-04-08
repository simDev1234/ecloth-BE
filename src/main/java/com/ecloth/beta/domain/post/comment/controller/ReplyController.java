package com.ecloth.beta.domain.post.comment.controller;

import com.ecloth.beta.domain.post.comment.dto.ReplyRequest;
import com.ecloth.beta.domain.post.comment.dto.ReplyResponse;
import com.ecloth.beta.domain.post.comment.service.ReplyService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 대댓글 API
 * - 포스팅 작성자만 대댓글을 달 수 있다.
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "대댓글API")
@RequestMapping("/api/feed/post/comment")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/{commentId}")
    public ResponseEntity<ReplyResponse> replyCreate(@PathVariable Long commentId,
                                                     @RequestBody ReplyRequest replyRequest) {

        ReplyResponse replyResponse = replyService.createReply(commentId, replyRequest);

        return ResponseEntity.ok(replyResponse);
    }

    @PutMapping("/reply/{replyId}")
    public ResponseEntity<ReplyResponse> replyUpdate(@PathVariable Long replyId,
                                                     @RequestBody ReplyRequest replyRequest) {

        ReplyResponse replyResponse = replyService.updateReply(replyId, replyRequest);

        return ResponseEntity.ok(replyResponse);
    }

    @DeleteMapping("/reply/{replyId}")
    public ResponseEntity<Void> replyDelete(@PathVariable Long replyId) {

        replyService.deleteReply(replyId);

        return ResponseEntity.ok().build();
    }
}
