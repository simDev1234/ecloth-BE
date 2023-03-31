package com.ecloth.beta.domain.post.posting.exception;

import lombok.Getter;

@Getter
public class PostingException extends RuntimeException{

    ErrorCode errorCode;

    public PostingException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public PostingException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public PostingException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public PostingException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public PostingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }
}
