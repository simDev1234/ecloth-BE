package com.ecloth.beta.member.dto;

import lombok.Getter;
import lombok.Setter;

public class MemberRequest {

    @Getter
    @Setter
    public static class Register {
        private String email;
        private String password;
        private String nickname;
        private String phone;
    }

    @Getter
    @Setter
    public static class Login {
        private String email;
        private String password;
    }

    @Getter
    @Setter
    public static class TokenRequest {
        private String accessToken;
        private String refreshToken;
    }

}
