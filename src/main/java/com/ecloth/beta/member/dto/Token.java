package com.ecloth.beta.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Token {

        private String accessToken;
        private String refreshToken;
        private Long refreshTokenExpirationTime;

}
