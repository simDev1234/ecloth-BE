package com.ecloth.beta.exception;

import com.ecloth.beta.follow.exception.ErrorCode;
import com.ecloth.beta.follow.exception.FollowException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleFollowException(FollowException e){

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = new ErrorResponse(errorCode.name(), errorCode.getMessage());

        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

}
