package com.ecloth.beta.member.controller;

import com.ecloth.beta.member.dto.MemberRequest;
import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.service.MemberService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Api(tags = "회원 API" )
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Member> memberRegister(@RequestBody MemberRequest.Register registerDto) {
        Member member = memberService.register(registerDto);
        return ResponseEntity.ok(member);
    }

    // 이메일 인증 확인
    @PostMapping("/register/email-auth")
    public ResponseEntity<Void> memberEmailAuth(@RequestParam("code") String emailAuthCode) {
        memberService.updateMemberAfterEmailAuth(emailAuthCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> memberLogin(@RequestBody MemberRequest.Login memberLoginDto, HttpServletResponse response) {
        HttpHeaders headers = memberService.login(memberLoginDto);
        String message = "로그인이 완료되었습니다.";
        return ResponseEntity.ok()
                .headers(headers)
                .body(message);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> memberLogout(@RequestHeader("Authorization") String accessToken) {
        memberService.logout(accessToken);
        return ResponseEntity.ok()
                .body("로그아웃 되었습니다.");
    }


    @PostMapping("/token/reissue")
    public ResponseEntity<String> tokenReissue(@RequestHeader("Authorization")String refreshToken) {
        HttpHeaders headers = memberService.reissueToken(refreshToken);
        String message = "토큰이 갱신되었습니다.";
        return ResponseEntity.ok()
                .headers(headers)
                .body(message);
    }
}
