package com.ecloth.beta.domain.follow.controller;

import com.ecloth.beta.domain.follow.dto.*;
import com.ecloth.beta.domain.follow.service.FollowService;
import com.ecloth.beta.domain.follow.type.PointDirection;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Locale;
import static com.ecloth.beta.domain.follow.type.PointDirection.FOLLOWS;


/**
 * 팔로우 API
 * - 팔로우 or 언팔로우 요청
 * - 팔로우 상태 조회 (닉네임,프로필,팔로우수/팔로워수 조회)
 * - 회원의 팔로우/팔로워 목록 조회
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Api(tags = "팔로우 API")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{memberId}/follow")
    public ResponseEntity<FollowingResponse> following(@PathVariable Long memberId,
                                                       @RequestBody FollowingRequest request){

        FollowingResponse response = followService.follow(memberId, request.getTargetId());

        return ResponseEntity.ok(response);

    }

    @GetMapping("/{memberId}/follow/status")
    public ResponseEntity<Boolean> followingStatus(@PathVariable Long memberId,
                                                   @RequestParam Long targetId){

        boolean response = followService.isFollowing(memberId, targetId);

        return  ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}/follow")
    public ResponseEntity<FollowingInfoResponse> memberFollowInfo(@PathVariable Long memberId){

        FollowingInfoResponse response = followService.findMemberFollowInfo(memberId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}/follows")
    public ResponseEntity<FollowListResponse> memberFollowList(@PathVariable Long memberId,
                                                               FollowListRequest request){

        PointDirection dir = PointDirection.valueOf(request.getDir().toUpperCase(Locale.ROOT));
        FollowListResponse response;

        if (FOLLOWS.equals(dir)) {
            response = followService.findFollowMemberList(memberId, request);
        } else {
            response = followService.findFollowerMemberList(memberId, request);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{memberId}/follow")
    public ResponseEntity<Void> unfollowing(@PathVariable Long memberId,
                                            @RequestBody FollowingRequest request){

        followService.unfollow(memberId, request.getTargetId());

        return ResponseEntity.ok().build();
    }

}
