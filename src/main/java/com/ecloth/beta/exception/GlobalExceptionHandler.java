package com.ecloth.beta.exception;

import com.ecloth.beta.follow.exception.ErrorCode;
import com.ecloth.beta.follow.exception.FollowException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e){

        ErrorResponse response = new ErrorResponse(e.toString(), e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
