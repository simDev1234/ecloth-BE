package com.ecloth.beta.chat.service;

import com.ecloth.beta.chat.document.ChatMessage;
import com.ecloth.beta.chat.dto.ChatMessageSendRequest;
import com.ecloth.beta.chat.dto.ChatMessageSendResponse;
import com.ecloth.beta.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
