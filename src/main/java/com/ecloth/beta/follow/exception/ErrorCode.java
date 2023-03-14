package com.ecloth.beta.follow.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ErrorCode {

    // TODO 회원단 작업 완료하면 UsernameNotFoundException으로 변경 필요
    FOLLOW_REQUESTER_NOT_FOUND("요청을 한 회원이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    FOLLOW_TARGET_NOT_FOUND("팔로우한(하려는) 회원이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    FOLLOW_DUPLICATE_REQUEST("이미 팔로우(또는 언팔로우)한 상태입니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

}
