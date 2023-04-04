package com.ecloth.beta.domain.member.controller;

import com.ecloth.beta.domain.member.dto.MemberLoginResponse;
import com.ecloth.beta.domain.member.dto.OauthToken;
import com.ecloth.beta.domain.member.service.OauthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@AllArgsConstructor
@Api(tags = "카카오 인증 API")
public class OauthController {

    private final OauthService oAuthService;

    @ApiOperation(value = "카카오 로그인", notes = "카카오계정을 통해 로그인을 진행한다.")
    @GetMapping("/KakaoLogin")
    public ResponseEntity<Object> kakaoLogin(@ApiIgnore @RequestParam("code") String authCode) {
        log.info("카카오 authcode 받음 : " + authCode);

        OauthToken oauthToken = oAuthService.getKakaoToken(authCode);
        MemberLoginResponse response = oAuthService.kakaoRegisterAndGetToken(oauthToken.getAccess_token());

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

}
