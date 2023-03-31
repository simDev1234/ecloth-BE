package com.ecloth.beta.exception;

import com.ecloth.beta.domain.chat.exception.ChatException;
import com.ecloth.beta.domain.follow.exception.ErrorCode;
import com.ecloth.beta.domain.follow.exception.FollowException;
import com.ecloth.beta.domain.member.exception.MemberErrorCode;
import com.ecloth.beta.domain.member.exception.MemberException;
import org.springframework.dao.DataIntegrityViolationException;
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
    public ResponseEntity<ErrorResponse> handleChatException(ChatException e){

        ErrorResponse response = new ErrorResponse(e.getErrorCode().name(), e.getErrorCode().getMessage());

        return new ResponseEntity<>(response, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorResponse> handleMemberException(MemberException e) {
        MemberErrorCode errorCode = e.getMemberErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(errorCode.name(), errorCode.getDetail());

        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }


    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e){

        ErrorResponse response = new ErrorResponse(e.toString(), e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e){

        ErrorResponse response = new ErrorResponse(e.toString(), e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
