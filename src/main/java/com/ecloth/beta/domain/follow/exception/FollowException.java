package com.ecloth.beta.domain.follow.exception;

import lombok.Getter;

@Getter
public class FollowException extends RuntimeException{

    ErrorCode errorCode;

    public FollowException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public FollowException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public FollowException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public FollowException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public FollowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }
}
