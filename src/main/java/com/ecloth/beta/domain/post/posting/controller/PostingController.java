package com.ecloth.beta.domain.post.posting.controller;

import com.ecloth.beta.domain.post.posting.dto.*;
import com.ecloth.beta.domain.post.posting.service.PostingService;
import com.ecloth.beta.security.memberDetail.MemberDetails;
import com.querydsl.core.util.ArrayUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.EntityNotFoundException;

/**
 * 포스트 API
 * - 포스트 CRUD *조회수 +1
 * - 포스트 좋아요
 */
@RestController
@RequestMapping("/api")
@Api(tags = "포스트 API")
@RequiredArgsConstructor
public class PostingController {

    private final PostingService postingService;

    // 포스트 등록
    @PostMapping(value = "/feed/post", consumes = {"multipart/form-data"})
    public ResponseEntity<?> postCreate(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails,
                                        PostingCreateRequest request) throws Exception {

        request.setMemberId(memberDetails.getMemberId());

        if (ArrayUtils.isEmpty(request.getImages()) || request.getImages().length == 0) {
            return new ResponseEntity<>("이미지를 1개 이상 등록해주세요.", HttpStatus.BAD_REQUEST);
        }

        postingService.createPost(request.getImages(), request);

        return ResponseEntity.ok().build();
    }

    // 게시글 목록 조회 - 필터 : 조회수순, 좋아요순, 최신순
    @GetMapping("/feed/post")
    public ResponseEntity<PostingListResponse> postListByPage(PostingListRequest request) {

        PostingListResponse response = postingService.getPostListByPage(request);

        return ResponseEntity.ok(response);
    }

    // 회원이 작성한 포스트 목록 조회
    @GetMapping("/feed/post/member/{memberId}")
    public ResponseEntity<MemberPostingListResponse> memberPostList(@PathVariable Long memberId,
                                                                    MemberPostingListRequest request) {

        MemberPostingListResponse response = postingService.getMemberPostList(memberId,request);

        return ResponseEntity.ok(response);
    }

    // 포스트 상세 조회
    @GetMapping("/feed/post/{postingId}")
    public ResponseEntity<PostingDetailResponse> postDetail(@PathVariable Long postingId) {

        PostingDetailResponse response = postingService.getPostDetail(postingId);

        return ResponseEntity.ok(response);
    }

    // 포스트 수정
    @PostMapping(value = "/feed/post/{postingId}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> postUpdate(@PathVariable Long postingId,
                                        @ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails,
                                        PostingUpdateRequest request) throws Exception {

        request.setMemberId(memberDetails.getMemberId());

        if (ArrayUtils.isEmpty(request.getImages()) || request.getImages().length == 0) {
            return new ResponseEntity<>("이미지를 1개 이상 등록해주세요.", HttpStatus.BAD_REQUEST);
        }

        postingService.updatePost(postingId, request.getImages(), request);

        return ResponseEntity.ok().build();
    }

    // 포스트 조회
    @GetMapping("/feed/post/{postingId}/like")
    public ResponseEntity<Boolean> postLikeStatus(@PathVariable Long postingId,
                                                  @ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails){

        Boolean response = postingService.isLikeTurnedOn(postingId, memberDetails.getMemberId());

        return ResponseEntity.ok(response);
    }

    // 포스트 좋아요 on/off
    @PutMapping("/feed/post/{postingId}/like")
    public ResponseEntity<Boolean> postLikeOnOrOff(@PathVariable Long postingId,
                                                   @ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails) {

        Boolean response = postingService.checkOrUnCheckLike(postingId, memberDetails.getMemberId());

        return ResponseEntity.ok(response);
    }

    // 포스트 삭제
    @DeleteMapping("/feed/post/{postingId}")
    public ResponseEntity<Void> postDelete(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails,
                                           @PathVariable Long postingId) {
        try {
            postingService.deletePost(postingId, memberDetails.getMemberId());
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

}

