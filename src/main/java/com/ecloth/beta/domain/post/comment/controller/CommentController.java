package com.ecloth.beta.domain.post.comment.controller;

import com.ecloth.beta.domain.post.comment.dto.CommentRequest;
import com.ecloth.beta.domain.post.comment.dto.CommentResponse;
import com.ecloth.beta.domain.post.comment.service.CommentService;
import com.ecloth.beta.domain.post.comment.dto.CommentListRequest;
import com.ecloth.beta.domain.post.comment.dto.CommentListResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * 댓글 API
 * - 댓글 CRUD
 */
@RestController
@Api(tags = "댓글 API")
@RequestMapping("/api/feed/post")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postingId}/comment")
    public ResponseEntity<CommentResponse> commentCreate(@PathVariable Long postingId,
                                                         @Valid @RequestBody CommentRequest commentRequest) {

        commentRequest.setPostingId(postingId);
        CommentResponse commentResponse = commentService.createComment(commentRequest);

        return ResponseEntity.ok(commentResponse);

    }

    @GetMapping("/{postingId}/comment")
    public ResponseEntity<CommentListResponse> commentList(@PathVariable Long postingId,
                                                       CommentListRequest request) {

        CommentListResponse response = commentService.getCommentListByPostingId(postingId, request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{postingId}/comment/{commentId}")
    public ResponseEntity<CommentResponse> comment(@PathVariable Long postingId,
                                                   @PathVariable Long commentId,
                                                   @RequestBody CommentRequest commentRequest) {

        commentRequest.setPostingId(postingId);
        CommentResponse commentResponse = commentService.updateContent(commentId, commentRequest);

        return ResponseEntity.ok(commentResponse);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {

        commentService.deleteComment(commentId);

        return ResponseEntity.ok().build();
    }

}
