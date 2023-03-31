package com.ecloth.beta.domain.chat.dto;

import com.ecloth.beta.domain.chat.document.ChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ChatMessageSendRequest {

    private Long chatRoomId;
    private Long writerId;
    private String message;

    public ChatMessage toEntity(){
        return ChatMessage.builder()
                .chatRoomId(this.chatRoomId)
                .writerId(this.writerId)
                .message(this.message)
                .registerDate(LocalDateTime.now())
                .build();
    }

}
