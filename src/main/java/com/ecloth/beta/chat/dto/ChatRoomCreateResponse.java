package com.ecloth.beta.chat.dto;

import com.ecloth.beta.chat.entity.ChatRoom;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChatRoomCreateResponse {

    private Long chatRoomId;

    public static ChatRoomCreateResponse fromEntity(ChatRoom newChatRoom) {
        return new ChatRoomCreateResponse(newChatRoom.getChatRoomId());
    }
}
