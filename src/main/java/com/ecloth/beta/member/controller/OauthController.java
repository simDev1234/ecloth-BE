package com.ecloth.beta.member.controller;

import com.ecloth.beta.member.dto.OauthToken;
import com.ecloth.beta.member.service.OauthService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@Api(tags = "카카오 로그인 API")
public class OauthController {

    private final OauthService oAuthService;

    // 프론트에서 인가코드 받기
    @GetMapping("/KakaoLogin")
    public ResponseEntity<Void> kakaoLogin(@RequestParam("code") String authCode) {
        log.info("카카오 authcode 받음 : " + authCode);

        OauthToken oauthToken = oAuthService.getKakaoToken(authCode);
        HttpHeaders headers = oAuthService.kakaoRegisterAndGetToken(oauthToken.getAccess_token());

        return ResponseEntity.ok().headers(headers).build();
    }

}
