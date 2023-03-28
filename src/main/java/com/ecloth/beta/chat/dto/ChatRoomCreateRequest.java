package com.ecloth.beta.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ChatRoomCreateRequest {

    @NotNull
    @Size(min = 2)
    private List<Long> memberIds;

}
