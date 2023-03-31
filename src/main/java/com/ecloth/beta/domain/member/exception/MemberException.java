package com.ecloth.beta.domain.member.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MemberException extends RuntimeException {
    MemberErrorCode memberErrorCode;

    public MemberException(MemberErrorCode memberErrorCode) {
        super(memberErrorCode.getDetail());
        this.memberErrorCode = memberErrorCode;
    }

    public HttpStatus getHttpStatus() {
        return memberErrorCode.getHttpStatus();
    }
}
