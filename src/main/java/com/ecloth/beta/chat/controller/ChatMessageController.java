package com.ecloth.beta.chat.controller;

import com.ecloth.beta.chat.dto.ChatMessageRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@Controller
public class ChatMessageController {

    @MessageMapping("/chat")
    public void receiveMessage(@ApiIgnore Principal principal, ChatMessageRequest request){

        String senderEmail = principal.getName();

    }

}
