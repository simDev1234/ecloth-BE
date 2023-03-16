package com.ecloth.beta.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;

}
