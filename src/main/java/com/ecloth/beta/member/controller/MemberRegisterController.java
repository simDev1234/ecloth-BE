package com.ecloth.beta.member.controller;

import com.ecloth.beta.member.dto.MemberRegisterDto;
import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.security.JwtTokenProvider;
import com.ecloth.beta.member.service.MemberRegisterService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class MemberRegisterController {

    private final MemberRegisterService memberRegisterService;

    @PostMapping("/register")
    public ResponseEntity<Member> register(@RequestBody MemberRegisterDto registerDto) {
        Member member = memberRegisterService.register(registerDto);
        return ResponseEntity.ok(member);
    }

    @PostMapping("/email-auth")
    public ResponseEntity<Void> emailAuth(@RequestParam("code") String emailAuthCode) {
        memberRegisterService.updateMemberAfterEmailAuth(emailAuthCode);
        return ResponseEntity.ok().build();
    }
}
