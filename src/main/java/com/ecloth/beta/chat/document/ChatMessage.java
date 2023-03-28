package com.ecloth.beta.chat.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Document(collection = "chat_messages")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    private ObjectId id;
    private Long chatRoomId;
    private Long writerId;
    private String message;
    private LocalDateTime registerDate;

}
