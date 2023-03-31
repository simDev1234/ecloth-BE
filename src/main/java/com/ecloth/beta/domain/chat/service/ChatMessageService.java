package com.ecloth.beta.domain.chat.service;

import com.ecloth.beta.domain.chat.document.ChatMessage;
import com.ecloth.beta.domain.chat.dto.ChatMessageSendRequest;
import com.ecloth.beta.domain.chat.dto.ChatMessageSendResponse;
import com.ecloth.beta.domain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public Optional<ChatMessage> findLatestMessage(Long chatRoomId) {
        return chatMessageRepository.findDistinctFirstByChatRoomIdOrderByRegisterDate(chatRoomId);
    }

    public ChatMessageSendResponse saveMessage(ChatMessageSendRequest request) {
        ChatMessage chatMessage = chatMessageRepository.save(request.toEntity());
        return ChatMessageSendResponse.fromEntity(chatMessage);
    }

}
