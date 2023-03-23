package com.ecloth.beta.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatMessageRequest {

    private Long receiverId;
    private String content;

}
