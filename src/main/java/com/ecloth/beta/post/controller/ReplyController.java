package com.ecloth.beta.post.controller;

import com.ecloth.beta.post.dto.ReplyRequest;
import com.ecloth.beta.post.dto.ReplyResponse;
import com.ecloth.beta.post.entity.Reply;
import com.ecloth.beta.post.service.ReplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/replies")
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    // 대댓글 조회 API
    @GetMapping("/{replyId}")
    public ResponseEntity<ReplyResponse> getReply() throws Exception {
        ReplyResponse reply = replyService.getReplyById();
        return ResponseEntity.ok(reply);
    }

    // 대댓글 작성 API
    @PostMapping
    public ResponseEntity<ReplyResponse> createReply(@RequestBody ReplyRequest request) throws Exception {
        Long replyId = replyService.createReply(request);
        ReplyResponse response = new ReplyResponse();
        return ResponseEntity.created(URI.create("/replies/" + replyId)).body(response);
    }

    // 대댓글 수정 API
    @PutMapping("/{replyId}")
    public ResponseEntity<ReplyResponse> updateReply(@PathVariable Long replyId, @RequestBody ReplyRequest request) throws Exception {
        request.setId(replyId);
        replyService.updateReply(request);
        ReplyResponse response = new ReplyResponse();
        return ResponseEntity.ok(response);
    }

    // 대댓글 삭제 API
    @DeleteMapping("/{replyId}")
    public ResponseEntity<ReplyResponse> deleteReply(@PathVariable Long replyId) throws Exception {
        replyService.deleteReply(replyId);
        ReplyResponse response = new ReplyResponse();
        return ResponseEntity.ok(response);
    }
}
