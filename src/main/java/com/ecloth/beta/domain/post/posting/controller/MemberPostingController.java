package com.ecloth.beta.domain.post.posting.controller;

import com.ecloth.beta.domain.post.posting.dto.MemberPostingListRequest;
import com.ecloth.beta.domain.post.posting.dto.MemberPostingListResponse;
import com.ecloth.beta.domain.post.posting.service.PostingService;
import com.ecloth.beta.security.memberDetail.MemberDetails;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;


@RestController
@RequestMapping("/api/member")
@Api(tags = "개인 피드 API")
@RequiredArgsConstructor
public class MemberPostingController {

    private final PostingService postingService;

    @GetMapping("/post")
    public ResponseEntity<MemberPostingListResponse> memberPostList(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails,
                                                                    MemberPostingListRequest request) {
        Long memberId = memberDetails.getMemberId();
        MemberPostingListResponse response = postingService.getMemberPostList(memberId, request);

        return ResponseEntity.ok(response);
    }
}