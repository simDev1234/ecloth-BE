package com.ecloth.beta.post.service;

import com.ecloth.beta.post.dto.PostingRequest;
import com.ecloth.beta.post.dto.PostingResponse;
import com.ecloth.beta.post.entity.Posting;
import com.ecloth.beta.post.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;


    public PostingResponse createPost(PostingRequest postingRequest) {
        Posting posting = Posting.builder()
                .postingId(postingRequest.getMemberId())
                .title(postingRequest.getTitle())
                .content(postingRequest.getContent())
                .imagePath(postingRequest.getImagePath())
                .build();

        Posting savedPosting = postingRepository.save(posting);

        return toPostResponse(savedPosting);
    }

    public PostingResponse getPost(Long postId) {
        Posting posting = getPostById(postId);
        return toPostResponse(posting);
    }

    public PostingResponse updatePost(Long postId, PostingRequest postingRequest) {
        Posting posting = getPostById(postId);

        posting.update(postingRequest.getTitle(), postingRequest.getContent(), postingRequest.getImagePath());

        return toPostResponse(posting);
    }


    public void deletePost(Long postId) {
        postingRepository.deleteById(postId);
    }

    private Posting getPostById(Long postId) {
//        return postingRepository.findById(postId).orElseThrow(() -> notfound());
        return postingRepository.findById(postId)
                .orElseThrow();
    }

    private PostingResponse toPostResponse(Posting posting) {
        return PostingResponse.builder()
                .postId(posting.getPostingId())
                .memberId(posting.getPostingId())
                .nickname(posting.getNickname())
                .profileImagePath(posting.getProfileImagePath())
                .title(posting.getTitle())
                .content(posting.getContent())
                .imagePath(posting.getImagePath())
                .likeCount(posting.getLikeCount())
                .viewCount(posting.getViewCount())
                .createdDate(posting.getCreatedDate())
                .updatedDate(posting.getUpdatedDate())
                .commentCount(posting.getCommentCount())
                .build();
    }

}

