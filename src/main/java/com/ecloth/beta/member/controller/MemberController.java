package com.ecloth.beta.member.controller;

import com.ecloth.beta.common.security.MemberDetails;
import com.ecloth.beta.member.dto.MemberGetInfoResponse;
import com.ecloth.beta.member.dto.MemberUpdateInfoRequest;
import com.ecloth.beta.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Api(tags = "회원 API")
public class MemberController {

    private final MemberService memberService;

    private Long getMemberId(MemberDetails memberDetails) {
        return Long.valueOf(memberDetails.getUsername());
    }

    private String getRole(MemberDetails memberDetails) {
        return memberDetails.getAuthorities().toString();
    }

    @ApiOperation(value = "마이페이지 정보조회", notes = "사용자의 이메일,닉네임,프로필이미지,전화번호를 조회한다.")
    @GetMapping("/member/me")
    public ResponseEntity<MemberGetInfoResponse> memberInfo(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails) {

        Long memberId = getMemberId(memberDetails);
        String role = getRole(memberDetails);
        MemberGetInfoResponse infoMe = memberService.getInfoMe(memberId, role);

        return ResponseEntity.ok(infoMe);
    }

    @ApiOperation(value = "마이페이지 정보수정", notes = "사용자의 닉네임,프로필이미지,전화번호,비밀번호를 변경한다.")
    @PutMapping("/member/me")
    public ResponseEntity<Void> updateMemberInfo(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails,
                                             MemberUpdateInfoRequest request) {

        Long memberId = getMemberId(memberDetails);
        memberService.updateInfoMe(memberId, request);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "나의 회원 id 조회", notes = "로그인 중인 회원의 회원 id를 조회한다.")
    @GetMapping("/member/me/id")
    public ResponseEntity<Long> myMemberId(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails) {

        Long memberId = getMemberId(memberDetails);
        memberService.getMemberId(memberId);

        return ResponseEntity.ok(memberId);
    }

    @ApiOperation(value = "회원 탈퇴", notes = "로그인 중인 회원의 회원 상태를 INACTIVE로 변경한다.")
    @PutMapping("/member/me/status")
    public ResponseEntity<Void> updateMemberStatus(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails,
                                                   HttpServletResponse response) {

        Long memberId = getMemberId(memberDetails);
        String role = getRole(memberDetails);
        memberService.updateMemberStatus(memberId,role);

        // 클라이언트의 refreshtoken 쿠키 삭제
        Cookie refreshTokenCookie = new Cookie("refreshtoken", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/api/token/reissue");
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok().build();
    }


}
