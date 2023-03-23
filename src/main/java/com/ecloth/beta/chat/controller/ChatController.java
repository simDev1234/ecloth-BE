package com.ecloth.beta.chat.controller;

import com.ecloth.beta.chat.dto.ChatListResponse;
import com.ecloth.beta.chat.service.ChatService;
import com.ecloth.beta.common.page.CustomPage;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import java.security.Principal;

/**
 * 채팅(룸) API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Api(tags = "채팅(룸) API")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/{memberId}")
    public ResponseEntity<?> chatCreate(@ApiIgnore Principal principal,
                                        @PathVariable Long memberId){

        chatService.createChat(principal.getName(), memberId);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> chatList(@ApiIgnore Principal principal,
                                      CustomPage requestPage){

        ChatListResponse response = chatService.findChatList(principal.getName(), requestPage);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> chatDelete(@ApiIgnore Principal principal,
                                        @PathVariable Long memberId){

        chatService.deleteChat(principal.getName(), memberId);

        return ResponseEntity.ok().build();
    }

}
