package com.ecloth.beta.member.controller;

import com.ecloth.beta.common.security.MemberDetails;
import com.ecloth.beta.member.dto.MemberRequest;
import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Api(tags = "회원인증 API")
public class AuthController {

    private final AuthService authService;

    private String getMemberId(MemberDetails memberDetails) {
        return memberDetails.getUsername();
    }

    private String getRole(MemberDetails memberDetails) {
        return memberDetails.getAuthorities().toString();
    }

    @ApiOperation(value = "회원 가입", notes = "회원 가입을 진행한다.")
    @PostMapping("/register")
    public ResponseEntity<Member> memberRegister(@RequestBody MemberRequest.Register registerDto) {

        Member member = authService.register(registerDto);

        return ResponseEntity.ok(member);
    }

    @ApiOperation(value = "이메일 확인", notes = "회원 가입 후 이메일로 발송된 코드를 통해 사용자 상태를 ACTIVE로 변경한다.")
    @PostMapping("/register/email-auth")
    public ResponseEntity<Void> memberEmailAuth(@RequestParam("code") String emailAuthCode) {

        authService.updateMemberAfterEmailAuth(emailAuthCode);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "로그인", notes = "이메일/패스워드를 통해 로그인을 진행한다.")
    @PostMapping("/login")
    public ResponseEntity<String> memberLogin(@RequestBody MemberRequest.Login memberLoginDto) {

        HttpHeaders headers = authService.login(memberLoginDto);
        String message = "로그인이 완료되었습니다.";

        return ResponseEntity.ok()
                .headers(headers)
                .body(message);
    }

    @ApiOperation(value = "로그아웃", notes = "사용자의 토큰을 삭제시키고 로그아웃 한다.")
    @PostMapping("/logout")
    public ResponseEntity<String> memberLogout(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails,
                                               @ApiIgnore @RequestHeader("Authorization") String accessToken) {

        String memberId = getMemberId(memberDetails);
        String role = getRole(memberDetails);
        authService.logout(memberId, role, accessToken);

        return ResponseEntity.ok()
                .body("로그아웃 되었습니다.");
    }

    @ApiOperation(value = "토큰 재발급", notes = "사용자의 AccessToken이 만료되었을 경우 RefreshToken을 통해 토큰 재발급을 진행한다.")
    @PostMapping("/token/reissue")
    public ResponseEntity<String> tokenReissue(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails) {

        String memberId = getMemberId(memberDetails);
        String role = getRole(memberDetails);
        HttpHeaders headers = authService.reissueToken(memberId, role);

        String message = "토큰이 갱신되었습니다.";
        return ResponseEntity.ok()
                .headers(headers)
                .body(message);
    }
}
