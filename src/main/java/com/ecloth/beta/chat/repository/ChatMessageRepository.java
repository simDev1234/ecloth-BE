package com.ecloth.beta.chat.repository;

import com.ecloth.beta.chat.document.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

}
