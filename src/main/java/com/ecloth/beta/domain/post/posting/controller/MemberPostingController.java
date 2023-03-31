package com.ecloth.beta.domain.post.posting.controller;

import com.ecloth.beta.domain.post.posting.service.PostingService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 개인 피드 API
 * - (협의 필요) 마이페이지 > 개인 피드
 * - 회원의 개인 피드
 */
@RestController
@RequestMapping("/api/member")
@Api(tags = "개인 피드 API")
@RequiredArgsConstructor
public class MemberPostingController {

    private final PostingService postingService;


}
