package com.ecloth.beta.domain.chat.dto;

import com.ecloth.beta.common.page.CustomPage;
import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

@Setter
@Getter
public class ChatRoomListRequest extends CustomPage {

    ChatRoomListRequest(int page, int size, String sortBy, String sortOrder){
        super(page, size, sortBy, sortOrder.toUpperCase(Locale.ROOT));
    }

}
