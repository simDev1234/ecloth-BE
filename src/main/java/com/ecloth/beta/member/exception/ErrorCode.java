package com.ecloth.beta.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400 BAD_REQUEST
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST,"ALREADY_USE_EMAIL","이미 가입된 이메일 입니다."),
    ALREADY_EXIST_NICKNAME(HttpStatus.BAD_REQUEST,"ALREADY_USE_NICKNAME","이미 사용중인 닉네임 입니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST,"WRONG_PASSWORD","잘못된 비밀번호 입니다."),
    INVALID_EMAIL_AUTH_CODE(HttpStatus.BAD_REQUEST,"INVALID_EMAIL_AUTH_CODE","유효하지 않은 이메일 인증 코드입니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.BAD_REQUEST,"NOT_FOUND_REFRESH_TOKEN","리프레시 토큰 정보를 확인 할 수 없습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "INVALID_TOKEN", "유효하지 않은 토큰입니다."),

    // 401 UNAUTHORIZED
    INVALID_AUTHENTICATION(HttpStatus.UNAUTHORIZED,"INVALID_AUTHENTICATION","유효하지 않은 인증 정보입니다."),
    ALREADY_LOGOUT_TOKEN(HttpStatus.UNAUTHORIZED,"ALREADY_LOGOUT_TOKEN","이미 로그아웃 된 토큰입니다. 다시 로그인 해주세요."),
    UNSURPPOTED_TOKEN(HttpStatus.UNAUTHORIZED,"UNSURPPOTED_TOKEN","알고리즘 타입이 다른 토큰입니다"),
    EMPTY_CLAIMS_TOKEN(HttpStatus.UNAUTHORIZED,"EMPTY_CLAIMS_TOKEN","토큰의 클레임정보가 비어있습니다"),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED,"EXPIRED_ACCESS_TOKEN", "만료된 액세스 토큰입니다."),
    INVALID_SIGNATURE_TOKEN(HttpStatus.UNAUTHORIZED,"INVALID_SIGNATURE_TOKEN","토큰의 시그니처가 유효하지 않습니다."),
    EXPIRED_OR_INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST,"EXPIRED_OR_INVALID_REFRESH_TOKEN","만료되었거나 유효하지 않은 리프레시 토큰입니다."),

    // 403 FORBIDDEN

    // 404 NOT_FOUND
    NOT_FOUND_USER(HttpStatus.NOT_FOUND,"NOT_FOUND_USER","가입된 회원이 없습니다."),

    // 500 INTERNAL_SERVER_ERROR
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"UNKNOWN_ERROR","알수없는 오류");

    private final HttpStatus httpStatus;
    private final String code;
    private final String detail;
}
