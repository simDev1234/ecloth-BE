package com.ecloth.beta.domain.member.controller;

import com.ecloth.beta.security.memberDetail.MemberDetails;
import com.ecloth.beta.domain.member.entity.Member;
import com.ecloth.beta.domain.member.dto.MemberLoginResponse;
import com.ecloth.beta.domain.member.dto.MemberPasswordUpdateRequest;
import com.ecloth.beta.domain.member.dto.MemberRequest;
import com.ecloth.beta.domain.member.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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
    public ResponseEntity<Member> memberRegister(@RequestBody MemberRequest.Register registerDto) throws MessagingException {

        Member member = authService.register(registerDto);

        return ResponseEntity.ok(member);
    }

    @ApiOperation(value = "이메일 인증", notes = "회원 가입 후 이메일로 발송된 코드를 통해 사용자 상태를 ACTIVE로 변경한다.")
    @PostMapping("/register/email-auth")
    public ResponseEntity<Void> memberEmailAuth(@RequestParam("code") String emailAuthCode) {

        authService.updateMemberAfterEmailAuth(emailAuthCode);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "로그인", notes = "이메일/패스워드를 통해 로그인을 진행한다.")
    @PostMapping("/login")
    public ResponseEntity<Object> memberLogin(@RequestBody MemberRequest.Login memberLoginDto) {

        MemberLoginResponse response = authService.login(memberLoginDto);

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @ApiOperation(value = "로그아웃", notes = "사용자의 토큰을 삭제시키고 로그아웃 한다.")
    @PostMapping("/logout")
    public ResponseEntity<String> memberLogout(@ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails,
                                               @ApiIgnore @RequestHeader("Authorization") String accessToken,
                                               HttpServletResponse response) {

        String memberId = getMemberId(memberDetails);
        String role = getRole(memberDetails);
        authService.logout(memberId, role, accessToken);

        // 클라이언트의 refreshtoken 쿠키 삭제
        Cookie refreshTokenCookie = new Cookie("refreshtoken", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/api/token/reissue");
        response.addCookie(refreshTokenCookie);

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

    @ApiOperation(value = "비밀번호 변경 코드 발송", notes = "회원가입된 사용자의 이메일로 비밀번호 변경 코드를 생성하여 전달한다.")
    @GetMapping("/member/resetPassword")
    public ResponseEntity<String> passwordReset(String email) throws MessagingException {
        authService.resetPassword(email);
        return ResponseEntity.ok().body("입력하신 이메일로 비밀번호 변경 코드를 전송했습니다.");
    }

    @ApiOperation(value = "비밀번호 변경 코드를 이용한 비밀번호 변경", notes = "비밀번호 초기화를 통해 발송된 비밀번호 변경 코드로 회원의 비밀번호룰 수정한다.")
    @PostMapping("/member/resetPassword/update")
    public ResponseEntity<String> passwordResetUpdate(MemberPasswordUpdateRequest request) {
        authService.resetPasswordUpdate(request);
        return ResponseEntity.ok().body("비밀번호 변경이 완료되었습니다.");
    }

}
