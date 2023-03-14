package com.ecloth.beta.member.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

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

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);

        }
    }

    @Getter
    @Setter
    public static class Logout {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Setter
    public static class Reissue {
        private String accessToken;
        private String refreshToken;
    }
}
