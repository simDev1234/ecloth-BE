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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Api(tags = "팔로우 API")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{memberId}/follow")
    public ResponseEntity<FollowingResponse> following(@ApiIgnore Principal principal,
                                                       @PathVariable Long memberId){

        //String requesterEmail = Objects.requireNonNull(principal.getName());
        FollowingResponse response = followService.createFollow("test@gmail.com", memberId);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/follow")
    public ResponseEntity<FollowingResponse> myFollowDetail(@ApiIgnore Principal principal){

        //FollowingResponse response = followService.findFollowDetailOfMine(principal.getName());
        FollowingResponse response = followService.findMyFollowDetail("test@gmail.com");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}/follow")
    public ResponseEntity<FollowingResponse> followDetailOfMember(@ApiIgnore Principal principal,
                                                                  @PathVariable long memberId){

        FollowingResponse response
                //  = followService.findFollowCountAndMyFollowingStatusOfTarget(principal.getName(), requesterId);
                = followService.findFollowDetailOfMember("test@gmail.com", memberId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/follows")
    public ResponseEntity<FollowListResponse> myFollowList(@ApiIgnore Principal principal,
                                                           FollowListRequest request){

//        FollowListResponse response = followService.findFollowListOfMine(principal.getName(),
//                PointDirection.valueOf(request.getPointDirection().toUpperCase(Locale.ROOT)), request.getPage());

        FollowListResponse response = followService.findMyFollowList("test@gmail.com",
                PointDirection.valueOf(request.getDir().toUpperCase(Locale.ROOT)), request.getCustomPage());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}/follows")
    public ResponseEntity<FollowListResponse> followListOfMember(@PathVariable long memberId,
                                                                 FollowListRequest request){

        FollowListResponse response = followService.findFollowListOfMember(memberId,
                PointDirection.valueOf(request.getDir().toUpperCase(Locale.ROOT)), request.getCustomPage());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{memberId}/follow")
    public ResponseEntity<Void> followStop(@ApiIgnore Principal principal,
                                           @PathVariable Long memberId){

        followService.stopFollowing(principal.getName(), memberId);

        return ResponseEntity.ok().build();
    }

}
