package com.ecloth.beta.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberPasswordUpdateRequest {
    private String code;
    private String newPassword;
}
