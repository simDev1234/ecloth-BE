package com.ecloth.beta.member.dto;

import org.springframework.http.HttpHeaders;

public class MemberLoginResponse {
    private final HttpHeaders headers;
    private final Object body;

    public MemberLoginResponse(HttpHeaders headers, Object body) {
        this.headers = headers;
        this.body = body;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public Object getBody() {
        return body;
    }
}
