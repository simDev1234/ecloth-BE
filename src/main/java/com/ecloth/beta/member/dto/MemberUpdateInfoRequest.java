package com.ecloth.beta.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateInfoRequest {
    private String nickname;
    private String phone;
    private String password;
    private String newPassword;
    private String profileImagePath;
}
