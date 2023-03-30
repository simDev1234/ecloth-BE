package com.ecloth.beta.chat.dto;

import com.ecloth.beta.chat.document.ChatMessage;
import com.ecloth.beta.chat.entity.ChatRoom;
import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.member.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatRoomListResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private long total;
    @Builder.Default
    private CustomPage page = new CustomPage(1, 5, "registerDate", "DESC");
    private List<ChatRoomInfo> chatRoomList;

    @AllArgsConstructor
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ChatRoomInfo implements Serializable{

        private Long chatRoomId;
        private Long partnerId;
        private String partnerNickname;
        private String partnerProfileImagePath;
        private String lastMessage;
        private LocalDateTime lastMessageDate;

        public static ChatRoomInfo fromEntity(long chatRoomId, Member partnerMember, ChatMessage chatMessage) {

            return ChatRoomInfo.builder()
                    .chatRoomId(chatRoomId)
                    .partnerId(partnerMember.getMemberId())
                    .partnerNickname(partnerMember.getNickname())
                    .partnerProfileImagePath(partnerMember.getProfileImagePath())
                    .lastMessage(chatMessage.getMessage())
                    .lastMessageDate(chatMessage.getRegisterDate())
                    .build();
        }

        public static ChatRoomInfo fromEntity(long chatRoomId, Member partnerMember){

            return ChatRoomInfo.builder()
                    .chatRoomId(chatRoomId)
                    .partnerId(partnerMember.getMemberId())
                    .partnerNickname(partnerMember.getNickname())
                    .partnerProfileImagePath(partnerMember.getProfileImagePath())
                    .build();
        }

        public static ChatRoomInfo of(ChatRoom room, Member partnerMember,
                                       Optional<ChatMessage> latestPartnerMessage) {
            return latestPartnerMessage.isEmpty() ?
                    ChatRoomInfo.fromEntity(room.getChatRoomId(), partnerMember) :
                    ChatRoomInfo.fromEntity(room.getChatRoomId(), partnerMember, latestPartnerMessage.get());
        }

    }

    public static ChatRoomListResponse fromEntity(long total, CustomPage pageResult,
                                                  List<ChatRoomInfo> chatRoomList) {

        return ChatRoomListResponse.builder()
                .total(total)
                .page(pageResult)
                .chatRoomList(chatRoomList)
                .build();
    }

}
