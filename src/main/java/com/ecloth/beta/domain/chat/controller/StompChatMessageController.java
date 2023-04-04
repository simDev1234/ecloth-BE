package com.ecloth.beta.domain.chat.controller;

import com.ecloth.beta.domain.chat.dto.ChatMessageSendRequest;
import com.ecloth.beta.domain.chat.dto.ChatMessageSendResponse;
import com.ecloth.beta.domain.chat.exception.ChatException;
import com.ecloth.beta.domain.chat.exception.ErrorCode;
import com.ecloth.beta.domain.chat.service.ChatMessageService;
import com.ecloth.beta.domain.chat.service.ChatRoomService;
import com.ecloth.beta.domain.member.repository.MemberRepository;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Api(tags = "채팅 메세지 API")
@Slf4j
public class StompChatMessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MemberRepository memberRepository;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat/enter")
    public ResponseEntity<?> chatRoomEnter(@RequestBody ChatMessageSendRequest request){

        log.info("StompChatController.chatRoomEnter : chatRoomId - {}, writerId - {}, message - {}",
                  request.getChatRoomId(), request.getWriterId(), request.getMessage());

        validateIfWriterIsMemberOfChatRoom(request);
        extractEnterMessageFromMemberNickname(request);
        ChatMessageSendResponse response = chatMessageResponseAfterSavingToMongoDB(request);
        simpMessagingTemplate.convertAndSend(subscriptionURI(request), response);

        return ResponseEntity.ok().build();
    }

    private void extractEnterMessageFromMemberNickname(ChatMessageSendRequest request) {
        String nickname = memberRepository.findNicknameByMemberId(request.getWriterId());
        request.setMessage(String.format("%s님이 들어왔습니다.", nickname));
    }

    @MessageMapping("/chat/message")
    public ResponseEntity<?> messageSend(@RequestBody ChatMessageSendRequest request){

        log.info("StompChatController.chatRoomEnter : chatRoomId - {}, writerId - {}, message - {}",
                  request.getChatRoomId(), request.getWriterId(), request.getMessage());

        validateIfWriterIsMemberOfChatRoom(request);
        ChatMessageSendResponse response = chatMessageResponseAfterSavingToMongoDB(request);
        simpMessagingTemplate.convertAndSend(subscriptionURI(request), response);

        return ResponseEntity.ok().build();
    }

    private void validateIfWriterIsMemberOfChatRoom(ChatMessageSendRequest request) throws ChatException{
        boolean isMemberOfChatRoom = chatRoomService.isMemberOfChatRoom(request.getChatRoomId(), request.getWriterId());
        if (!isMemberOfChatRoom) {
            log.warn("StompChatMessageController.validateIfWriterIsMemberOfChatRoom : Member does not belong to chat room");
            throw new MessageDeliveryException("Member does not belong to Chat Room");
        }
    }

    private String subscriptionURI(ChatMessageSendRequest request) {
        return String.format("/queue/chat/%d", request.getChatRoomId());
    }

    private ChatMessageSendResponse chatMessageResponseAfterSavingToMongoDB(ChatMessageSendRequest request) {
        return chatMessageService.saveMessage(request);
    }

}
