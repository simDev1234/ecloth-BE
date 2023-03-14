package com.ecloth.beta.member.controller;

import com.ecloth.beta.member.dto.MemberRequest;
import com.ecloth.beta.member.dto.Token;
import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Member> register(@RequestBody MemberRequest.Register registerDto) {
        Member member = memberService.register(registerDto);
        return ResponseEntity.ok(member);
    }

    // 이메일 인증 확인
    @PostMapping("/register/email-auth/check")
    public ResponseEntity<Void> emailAuth(@RequestParam("code") String emailAuthCode) {
        memberService.updateMemberAfterEmailAuth(emailAuthCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public Token login(@RequestBody MemberRequest.Login memberLoginDto) {
        return memberService.login(memberLoginDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String accessToken) {
        memberService.logout(accessToken);
        return ResponseEntity.ok()
                .body("로그아웃 되었습니다.");
    }
}
