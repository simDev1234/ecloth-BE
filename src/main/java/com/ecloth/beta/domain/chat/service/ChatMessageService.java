package com.ecloth.beta.domain.chat.service;

import com.ecloth.beta.domain.chat.document.ChatMessage;
import com.ecloth.beta.domain.chat.dto.ChatMessageSendRequest;
import com.ecloth.beta.domain.chat.dto.ChatMessageListResponse;
import com.ecloth.beta.domain.chat.dto.ChatMessageListResponse.ChatMessageResponse;
import com.ecloth.beta.domain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public Optional<ChatMessage> findLatestMessage(Long chatRoomId) {
        return chatMessageRepository.findDistinctFirstByChatRoomIdOrderByRegisterDate(chatRoomId);
    }

    public ChatMessageListResponse saveMessageAndGetMessageList(ChatMessageSendRequest request) {

        chatMessageRepository.save(request.toEntity());

        return ChatMessageListResponse.fromEntity(chatMessageRepository.findAll());
    }

}
