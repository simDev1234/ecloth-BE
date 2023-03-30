package com.ecloth.beta.chat.repository;

import com.ecloth.beta.chat.document.ChatMessage;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, ObjectId> {

    Optional<ChatMessage> findDistinctFirstByChatRoomIdOrderByRegisterDate(Long chatRoomId);

}
