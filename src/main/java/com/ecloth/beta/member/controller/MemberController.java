package com.ecloth.beta.member.controller;

import com.ecloth.beta.common.security.MemberDetails;
import com.ecloth.beta.member.dto.InfoMeResponse;
import com.ecloth.beta.member.dto.InfoMeUpdateRequest;
import com.ecloth.beta.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Api(tags = "회원 API")
public class MemberController {

    private final MemberService memberService;

    private String getMemberId(MemberDetails memberDetails) {
        return memberDetails.getUsername();
    }

    private String getRole(MemberDetails memberDetails) {
        return memberDetails.getAuthorities().toString();
    }

    @ApiOperation(value = "마이페이지 정보조회", notes = "사용자의 이메일,닉네임,프로필이미지,전화번호를 조회한다.")
    @GetMapping("/member/me")
    public ResponseEntity<InfoMeResponse> infoMe(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails) {

        String memberId = getMemberId(memberDetails);
        String role = getRole(memberDetails);
        InfoMeResponse infoMe = memberService.getInfoMe(memberId, role);

        return ResponseEntity.ok(infoMe);
    }

    @ApiOperation(value = "마이페이지 정보수정", notes = "사용자의 닉네임,프로필이미지,전화번호,비밀번호를 변경한다.")
    @PutMapping("/member/me")
    public ResponseEntity<Void> updateInfoMe(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails,
                                             InfoMeUpdateRequest request) {

        String memberId = getMemberId(memberDetails);
        memberService.updateInfoMe(memberId, request);

        return ResponseEntity.ok().build();
    }
}
