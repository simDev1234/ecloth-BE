package com.ecloth.beta.member.dto;

import lombok.Data;

@Data
public class MemberRegisterDto {
    private String email;
    private String password;
    private String nickname;
    private String phone;
}
