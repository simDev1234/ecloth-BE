package com.ecloth.beta.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatMessageSendRequest {

    private Long chatRoomId;
    private Long writerId;
    private String message;

}
