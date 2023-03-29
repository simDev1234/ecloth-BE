package com.ecloth.beta.chat.controller;

import com.ecloth.beta.chat.dto.ChatMessageSendRequest;
import com.ecloth.beta.chat.dto.ChatMessageSendResponse;
import com.ecloth.beta.chat.exception.ChatException;
import com.ecloth.beta.chat.exception.ErrorCode;
import com.ecloth.beta.chat.service.ChatMessageService;
import com.ecloth.beta.chat.service.ChatRoomService;
import com.ecloth.beta.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class StompChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MemberRepository memberRepository;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/enter")
    public void chatRoomEnter(ChatMessageSendRequest request){
        validateIfWriterIsMemberOfChatRoom(request);
        extractEnterMessageFromMemberNickname(request);
        ChatMessageSendResponse response = chatMessageResponseAfterSavingToMongoDB(request);
        simpMessagingTemplate.convertAndSend(subscriptionURI(request), response);
    }

    private void extractEnterMessageFromMemberNickname(ChatMessageSendRequest request) {
        String nickname = memberRepository.findNicknameByMemberId(request.getWriterId());
        request.setMessage(String.format("%s님이 들어왔습니다.", nickname));
    }

    @MessageMapping("/message")
    public void messageSend(ChatMessageSendRequest request){
        validateIfWriterIsMemberOfChatRoom(request);
        ChatMessageSendResponse response = chatMessageResponseAfterSavingToMongoDB(request);
        simpMessagingTemplate.convertAndSend(subscriptionURI(request), response);
    }

    private void validateIfWriterIsMemberOfChatRoom(ChatMessageSendRequest request) {
        boolean isMemberOfChatRoom
                = chatRoomService.isMemberOfChatRoom(request.getChatRoomId(), request.getWriterId());
        if (!isMemberOfChatRoom) {
            throw new ChatException(ErrorCode.NOT_MEMBER_OF_CHAT_ROOM);
        }
    }

    private static String subscriptionURI(ChatMessageSendRequest request) {
        return String.format("/queue/chat/room/%d", request.getChatRoomId());
    }

    private ChatMessageSendResponse chatMessageResponseAfterSavingToMongoDB(ChatMessageSendRequest request) {
        return chatMessageService.saveMessage(request);
    }

}
