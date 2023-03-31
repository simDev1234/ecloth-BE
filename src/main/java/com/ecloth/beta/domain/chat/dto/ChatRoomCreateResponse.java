package com.ecloth.beta.domain.chat.dto;

import com.ecloth.beta.domain.chat.entity.ChatRoom;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class ChatRoomCreateResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long chatRoomId;

    public static ChatRoomCreateResponse fromEntity(ChatRoom newChatRoom) {
        return new ChatRoomCreateResponse(newChatRoom.getChatRoomId());
    }
}
