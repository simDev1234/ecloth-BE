package com.ecloth.beta.domain.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ChatRoomExitRequest {

    private Long chatRoomId;
    private Long memberId;

}
