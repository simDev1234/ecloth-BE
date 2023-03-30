package com.ecloth.beta.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ChatRoomExitRequest {

    private Long chatMemberId;
    private Long memberId;

}
