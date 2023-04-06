package com.ecloth.beta.domain.member.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MemberUpdateInfoRequest {
    private String nickname;
    private String phone;
    private String password;
    private String newPassword;
    private MultipartFile profileImage;
}
