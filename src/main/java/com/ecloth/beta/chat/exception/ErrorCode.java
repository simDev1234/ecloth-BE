package com.ecloth.beta.chat.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    CHAT_ROOM_NOT_FOUND("요청한 채팅룸은 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_MEMBER_OF_CHAT_ROOM("해당 채팅룸에 입장한 적이 없는 회원입니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

}
