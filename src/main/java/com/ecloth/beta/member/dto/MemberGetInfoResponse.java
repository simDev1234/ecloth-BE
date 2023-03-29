package com.ecloth.beta.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberGetInfoResponse {

    private String email;
    private String nickname;
    private String phone;
    private String profileImagePath;
    private String role;

}