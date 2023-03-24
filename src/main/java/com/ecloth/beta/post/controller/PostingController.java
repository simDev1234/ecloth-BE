package com.ecloth.beta.post.controller;

import com.ecloth.beta.post.dto.PostRequest;
import com.ecloth.beta.post.entity.Posting;
import com.ecloth.beta.post.service.PostingService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Api(tags = "게시글API")
public class PostingController {

    private final PostingService postingService;

    @GetMapping
    public List<Posting> getAllPosts() {
        return postingService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Posting> getPostById(@PathVariable Long id) {
        Posting posting = postingService.getPostById(id);
        if (posting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(posting, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest) {

        return new ResponseEntity<>(postingService.createPost(postRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Posting> updatePost(@PathVariable Long id, @RequestBody Posting posting) {
        Posting updatedPosting = postingService.updatePost(id, posting);
        if (updatedPosting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedPosting, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        boolean deleted = postingService.deletePost(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}

