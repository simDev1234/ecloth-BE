package com.ecloth.beta.domain.chat.dto;

import com.ecloth.beta.domain.chat.document.ChatMessage;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatMessageListResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ChatMessageResponse> chatMessages;

    @AllArgsConstructor
    @Builder
    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ChatMessageResponse implements Serializable{

        private static final long serialVersionUID = 1L;

        private Long chatRoomId;
        private Long writerId;
        private String message;
        private LocalDateTime registerDate;

        public static ChatMessageResponse fromEntity(ChatMessage chatMessage) {
            return ChatMessageResponse.builder()
                    .chatRoomId(chatMessage.getChatRoomId())
                    .writerId(chatMessage.getWriterId())
                    .message(chatMessage.getMessage())
                    .registerDate(chatMessage.getRegisterDate())
                    .build();
        }

    }

    public static ChatMessageListResponse fromEntity(List<ChatMessage> chatMessages) {
        return ChatMessageListResponse.builder()
                .chatMessages(chatMessages.stream().map(ChatMessageResponse::fromEntity).collect(Collectors.toList()))
                .build();
    }

}
