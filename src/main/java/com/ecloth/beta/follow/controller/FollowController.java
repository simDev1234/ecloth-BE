package com.ecloth.beta.follow.controller;

import com.ecloth.beta.follow.dto.FollowListRequest;
import com.ecloth.beta.follow.dto.FollowListResponse;
import com.ecloth.beta.follow.dto.FollowingRequest;
import com.ecloth.beta.follow.dto.FollowingResponse;
import com.ecloth.beta.follow.service.FollowService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import java.security.Principal;
import static com.ecloth.beta.follow.type.PointDirection.FOLLOWS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Api(tags = "팔로우 API")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity<FollowingResponse> followSave(@ApiIgnore Principal principal,
                                                        @RequestBody FollowingRequest request){

        String requesterEmail = principal.getName();
        FollowingResponse response = followService.saveFollow(requesterEmail, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/follow")
    public ResponseEntity<FollowingResponse> followCountOfMine(@ApiIgnore Principal principal){

        FollowingResponse response = followService.findFollowCountOfMine(principal.getName());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{requesterId}/follow")
    public ResponseEntity<FollowingResponse> followCountAndMyFollowingStatusOfTarget(@ApiIgnore Principal principal,
                                                                                     @PathVariable long requesterId){

        FollowingResponse response
                = followService.findFollowCountAndMyFollowingStatusOfTarget(principal.getName(), requesterId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/follows")
    public ResponseEntity<FollowListResponse> followListOfMine(@ApiIgnore Principal principal,
                                                               @RequestBody FollowListRequest request){

        FollowListResponse response;
        if (FOLLOWS.name().equals(request.getPointDirection())) {
            response = followService.findFollowListOfMine(principal.getName(), request.getPage());
        } else {
            response = followService.findFollowerListOfMine(principal.getName(), request.getPage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{requesterId}/follows")
    public ResponseEntity<FollowListResponse> followListOfTarget(@PathVariable long requesterId,
                                                                 @RequestBody FollowListRequest request){

        FollowListResponse response;
        if (FOLLOWS.name().equals(request.getPointDirection())) {
            response = followService.findFollowListOf(requesterId, request.getPage());
        } else {
            response = followService.findFollowerListOf(requesterId, request.getPage());
        }

        return ResponseEntity.ok(response);
    }

}
