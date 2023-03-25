package com.ecloth.beta.post.controller;

import com.ecloth.beta.post.dto.PostingRequest;
import com.ecloth.beta.post.dto.PostingResponse;
import com.ecloth.beta.post.service.PostingService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@Api(tags = "포스트API")
@RequiredArgsConstructor
public class PostingController {

    private final PostingService postingService;

    @PostMapping
    public PostingResponse createPost(@RequestBody PostingRequest postingRequest) {
        return postingService.createPost(postingRequest);
    }

    @GetMapping("/{postId}")
    public PostingResponse getPost(@PathVariable Long postId) {
        return postingService.getPost(postId);
    }

    @PutMapping("/{postId}")
    public PostingResponse updatePost(@PathVariable Long postId, @RequestBody PostingRequest postingRequest) {
        return postingService.updatePost(postId, postingRequest);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postingService.deletePost(postId);
    }
}


