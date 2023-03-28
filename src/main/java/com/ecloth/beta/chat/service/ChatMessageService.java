package com.ecloth.beta.chat.service;

import com.ecloth.beta.chat.document.ChatMessage;
import com.ecloth.beta.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public Optional<ChatMessage> findLatestPartnerMessage(Long chatRoomId, Long partnerMemberId) {
        return chatMessageRepository.findDistinctFirstByChatRoomIdAndWriterIdOrderByRegisterDate(chatRoomId, partnerMemberId);
    }

}
