package com.ecloth.beta.domain.post.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    COMMENT_NOT_FOUND("해당 댓글은 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    REPLY_NOT_FOUND("해당 대댓글은 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    POSTING_WRITER_REPLY_WRITER_NOT_MATCHING("포스트 작성자만 대댓글을 달 수 있습니다.", HttpStatus.BAD_REQUEST),
    NOT_REPLY_WRITER("대댓글 작성자만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST),
    NOT_COMMENT_WRITER("댓글은 작성자만 수정할 수 있습니다.",HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

}
