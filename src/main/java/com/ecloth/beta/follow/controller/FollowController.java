package com.ecloth.beta.follow.controller;

import com.ecloth.beta.follow.dto.FollowList;
import com.ecloth.beta.follow.dto.Following;
import com.ecloth.beta.follow.exception.FollowException;
import com.ecloth.beta.follow.service.FollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import java.security.Principal;
import static com.ecloth.beta.follow.exception.ErrorCode.FOLLOW_REQUEST_NOT_VALID;
import static com.ecloth.beta.follow.exception.ErrorCode.UNFOLLOW_REQUEST_NOT_VALID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Api(tags = "팔로우 API")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity<Following.Response> follow(@ApiIgnore Principal principal,
                                    @RequestBody Following.Request request){

        if (request.isFollowStatus() == false) {
            throw new FollowException(FOLLOW_REQUEST_NOT_VALID);
        }

        String requesterEmail = principal.getName();
        Following.Response response = followService.followOrUnFollowTarget(requesterEmail, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/follow")
    public ResponseEntity<Following.Response> getFollowStatus(@ApiIgnore Principal principal,
                                                             @RequestParam Long targetId){

        Following.Response response = followService.getFollowStatus(principal.getName(), targetId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/follows")
    public ResponseEntity<FollowList.Response> getFollowList(@ApiIgnore Principal principal,
                                                             @RequestBody FollowList.Request request){

        FollowList.Response response = followService.getFollowList(principal.getName(), request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/follow")
    public ResponseEntity<Following.Response> unfollow(@ApiIgnore Principal principal,
                                                        @RequestBody Following.Request request){

        if (request.isFollowStatus() == true) {
            throw new FollowException(UNFOLLOW_REQUEST_NOT_VALID);
        }

        String requesterEmail = principal.getName();
        Following.Response response = followService.followOrUnFollowTarget(requesterEmail, request);

        return ResponseEntity.ok(response);
    }

}
