package com.ecloth.beta.post.controller;

import com.ecloth.beta.post.dto.CommentRequest;
import com.ecloth.beta.post.dto.CommentResponse;
import com.ecloth.beta.post.service.CommentService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "댓글API")
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest) {
        CommentResponse commentResponse = commentService.createComment(commentRequest);
        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getComment(@PathVariable Long commentId) {
        CommentResponse commentResponse = commentService.getComment(commentId);
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId,
                                                         @RequestBody CommentRequest commentRequest) {
        CommentResponse commentResponse = commentService.updateComment(commentId, commentRequest);
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
