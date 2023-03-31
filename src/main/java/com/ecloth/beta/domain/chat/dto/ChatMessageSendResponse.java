package com.ecloth.beta.domain.chat.dto;

import com.ecloth.beta.domain.chat.document.ChatMessage;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatMessageSendResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long chatRoomId;
    private Long writerId;
    private String message;
    private LocalDateTime registerDate;

    public static ChatMessageSendResponse fromEntity(ChatMessage chatMessage) {
        return ChatMessageSendResponse.builder()
                .chatRoomId(chatMessage.getChatRoomId())
                .writerId(chatMessage.getWriterId())
                .message(chatMessage.getMessage())
                .registerDate(chatMessage.getRegisterDate())
                .build();
    }

}
