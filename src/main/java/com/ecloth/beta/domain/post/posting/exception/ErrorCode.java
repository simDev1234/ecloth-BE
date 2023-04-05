package com.ecloth.beta.domain.post.posting.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    POSTING_NOT_FOUND("해당 포스트는 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    POSTING_WRITER_NOT_MATCHING("포스트 작성자가 아닙니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

}
