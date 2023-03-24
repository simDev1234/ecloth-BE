package com.ecloth.beta.post.controller;

import com.ecloth.beta.post.dto.CommentRequest;
import com.ecloth.beta.post.entity.Comment;
import com.ecloth.beta.post.service.CommentService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@Api(tags = "댓글API")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Optional<Comment>> createComment(@RequestBody @Validated CommentRequest request) {
        Optional<Comment> response = commentService.createComment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable Long id) {
        Comment response = commentService.getComment(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody CommentRequest request) {
        Comment response = commentService.updateComment(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }


}

