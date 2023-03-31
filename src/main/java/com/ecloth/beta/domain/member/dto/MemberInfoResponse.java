package com.ecloth.beta.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberInfoResponse {

    private String email;
    private String nickname;
    private String phone;
    private String profileImagePath;
    private String role;

}
