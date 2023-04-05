package com.ecloth.beta.domain.chat.dto;

import com.ecloth.beta.domain.chat.entity.ChatRoom;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatRoomCreateResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long chatRoomId;

    public static ChatRoomCreateResponse fromEntity(ChatRoom newChatRoom) {
        return new ChatRoomCreateResponse(newChatRoom.getChatRoomId());
    }
}
