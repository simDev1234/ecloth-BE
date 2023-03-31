package com.ecloth.beta.domain.chat.exception;

import lombok.Getter;

@Getter
public class ChatException extends RuntimeException{

    ErrorCode errorCode;

    public ChatException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ChatException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ChatException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ChatException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ChatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }
}
