package com.ecloth.beta.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import springfox.documentation.annotations.ApiIgnore;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/{memberId}")
    @SendTo("/")
    public void chatSubscribe(@ApiIgnore Principal principal){



    }

}
