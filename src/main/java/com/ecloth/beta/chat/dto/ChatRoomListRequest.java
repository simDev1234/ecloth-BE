package com.ecloth.beta.chat.dto;

import com.ecloth.beta.common.page.CustomPage;
import lombok.Getter;
import lombok.Setter;
import java.util.Locale;

@Setter
@Getter
public class ChatRoomListRequest extends CustomPage {

    ChatRoomListRequest(){
        super(1, 5, "registerDate", "DESC");
    }

    ChatRoomListRequest(int page){
        super(page, 5, "registerDate", "DESC");
    }

    ChatRoomListRequest(int page, int size){
        super(page, size, "registerDate", "DESC");
    }

    ChatRoomListRequest(int page, int size, String sortBy){
        super(page, size, sortBy, "DESC");
    }

    ChatRoomListRequest(int page, int size, String sortBy, String sortOrder){
        super(page, size, sortBy, sortOrder.toUpperCase(Locale.ROOT));
    }

}
