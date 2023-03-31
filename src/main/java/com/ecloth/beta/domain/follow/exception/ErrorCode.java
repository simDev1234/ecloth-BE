package com.ecloth.beta.domain.follow.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    FOLLOW_DUPLICATE_REQUEST("이미 팔로우(또는 언팔로우)한 상태입니다.", HttpStatus.BAD_REQUEST),
    FOLLOW_NOT_FOUND("해당 타켓을 팔로우한 기록이 없습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

}
