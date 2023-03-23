package com.ecloth.beta.follow.controller;

import com.ecloth.beta.follow.dto.FollowListRequest;
import com.ecloth.beta.follow.dto.FollowListResponse;
import com.ecloth.beta.follow.dto.FollowingResponse;
import com.ecloth.beta.follow.service.FollowService;
import com.ecloth.beta.follow.type.PointDirection;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import java.security.Principal;
import java.util.Locale;
import static com.ecloth.beta.follow.type.PointDirection.FOLLOWS;

/**
 * 팔로우 API
 * - 팔로우 or 언팔로우 요청
 * - 팔로우 상태 조회
 * - 회원의 팔로우수/팔로워수 조회
 * - 회원의 팔로우/팔로워 목록 조회
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Api(tags = "팔로우 API")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{memberId}/follow")
    public ResponseEntity<FollowingResponse> following(@ApiIgnore Principal principal,
                                                       @PathVariable Long memberId){

        FollowingResponse response = followService.follow(principal.getName(), memberId);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{memberId}/follow/status")
    public ResponseEntity<Boolean> followingStatus(@ApiIgnore Principal principal,
                                                   @PathVariable Long memberId){

        boolean response = followService.isFollowing(principal.getName(), memberId);
        return  ResponseEntity.ok(response);

    }

    @GetMapping("/follow")
    public ResponseEntity<FollowingResponse> memberFollowInfo(@ApiIgnore Principal principal){

        FollowingResponse response = followService.findMemberFollowInfo(principal.getName());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}/follow")
    public ResponseEntity<FollowingResponse> memberFollowInfo(@ApiIgnore Principal principal,
                                                              @PathVariable long memberId){

        FollowingResponse response = followService.findMemberFollowInfo(principal.getName(), memberId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/follows")
    public ResponseEntity<FollowListResponse> memberFollowList(@ApiIgnore Principal principal,
                                                               FollowListRequest request){

        PointDirection dir = PointDirection.valueOf(request.getDir().toUpperCase(Locale.ROOT));
        FollowListResponse response;

        if (FOLLOWS.equals(dir)) {
            response = followService.findFollowList(principal.getName(), request.getCustomPage());
        } else {
            response = followService.findFollowerList(principal.getName(), request.getCustomPage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}/follows")
    public ResponseEntity<FollowListResponse> memberFollowList(@PathVariable long memberId,
                                                                 FollowListRequest request){

        PointDirection dir = PointDirection.valueOf(request.getDir().toUpperCase(Locale.ROOT));
        FollowListResponse response;

        if (FOLLOWS.equals(dir)) {
            response = followService.findFollowList(memberId, request.getCustomPage());
        } else {
            response = followService.findFollowerList(memberId, request.getCustomPage());
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{memberId}/follow")
    public ResponseEntity<Void> unfollowing(@ApiIgnore Principal principal,
                                            @PathVariable Long memberId){

        followService.unfollow(principal.getName(), memberId);

        return ResponseEntity.ok().build();
    }

}
