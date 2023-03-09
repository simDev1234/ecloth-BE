package com.ecloth.beta.member.model;

public enum MemberStatus {
    /*
    미인증,사용,정지,탈퇴
    */
    UNVERIFIED("Unverified"),
    ACTIVE("Active"),
    SUSPENDED("Suspended"),
    INACTIVE("Inactive");
    private final String status;

    MemberStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
