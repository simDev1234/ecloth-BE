package com.ecloth.beta.domain.member.controller;

import com.ecloth.beta.security.memberDetail.MemberDetails;
import com.ecloth.beta.domain.member.dto.MemberInfoResponse;
import com.ecloth.beta.domain.member.dto.MemberLocationUpdateRequest;
import com.ecloth.beta.domain.member.dto.MemberUpdateInfoRequest;
import com.ecloth.beta.domain.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/member")
@AllArgsConstructor
@Api(tags = "회원 API")
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "마이페이지 정보조회", notes = "사용자의 이메일,닉네임,프로필이미지,전화번호를 조회한다.")
    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> memberInfo(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails) {

        MemberInfoResponse infoMe = memberService.getMemberInfo(memberDetails.getMemberId(), memberDetails.getRole());

        return ResponseEntity.ok(infoMe);
    }

    @ApiOperation(value = "마이페이지 정보수정", notes = "사용자의 닉네임,프로필이미지,전화번호,비밀번호를 변경한다.")
    @PutMapping("/me")
    public ResponseEntity<Void> updateMemberInfo(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails,
                                                 MemberUpdateInfoRequest request) {

        memberService.updateMemberInfo(memberDetails.getMemberId(), request);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "나의 회원 id 조회", notes = "로그인 중인 회원의 회원 id를 조회한다.")
    @GetMapping("/me/id")
    public ResponseEntity<Long> myMemberId(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails) {

        Long memberId = memberDetails.getMemberId();
        memberService.getMemberId(memberId);

        return ResponseEntity.ok(memberId);
    }

    @ApiOperation(value = "회원 탈퇴", notes = "로그인 중인 회원의 회원 상태를 INACTIVE로 변경한다.")
    @PutMapping("/me/status")
    public ResponseEntity<Void> updateMemberStatus(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails,
                                                   HttpServletResponse response) {

        memberService.updateMemberStatus(memberDetails.getMemberId(),  memberDetails.getRole());
        deleteRefreshTokenFromClientCookie(response);

        return ResponseEntity.ok().build();
    }

    private static void deleteRefreshTokenFromClientCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshtoken", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/api/token/reissue");
        response.addCookie(refreshTokenCookie);
    }

    @PutMapping("/locations")
    public ResponseEntity<Void> updateLocation(@PathVariable Long memberId,
                                               @RequestBody MemberLocationUpdateRequest request) {

        memberService.updateMemberLocation(memberId, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
