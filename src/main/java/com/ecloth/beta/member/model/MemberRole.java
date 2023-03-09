package com.ecloth.beta.member.model;

public enum MemberRole {
    EMAIL_MEMBER("EmailMember"),
    KAKAO_MEMBER("KakaoMember"),
    ADMIN("Admin");

    private final String role;

    MemberRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}

