package com.ecloth.beta.chat.dto;

import com.ecloth.beta.chat.document.ChatMessage;
import com.ecloth.beta.chat.entity.Chat;
import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.member.entity.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatListResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private long total;
    @Builder.Default
    private CustomPage page = new CustomPage(1, 5, "registerDate", "DESC");
    private List<ChatPartnerInfo> partnerList = new ArrayList<>();

    @AllArgsConstructor
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ChatPartnerInfo implements Serializable{

        private Long memberId;
        private String nickname;
        private String profileImagePath;
        private String recentMsg;
        private LocalDateTime recentMsgDate;

        public static ChatPartnerInfo fromEntity(Member member, ChatMessage chatMessage) {
            return ChatPartnerInfo.builder()
                    .memberId(member.getMemberId())
                    .nickname(member.getNickname())
                    .profileImagePath(member.getProfileImagePath())
                    .recentMsg(chatMessage.getContent())
                    .recentMsgDate(chatMessage.getRegisterDate())
                    .build();
        }

        public static ChatPartnerInfo fromEntity(Member member) {
            return ChatPartnerInfo.builder()
                    .memberId(member.getMemberId())
                    .nickname(member.getNickname())
                    .profileImagePath(member.getProfileImagePath())
                    .build();
        }

    }

    public static ChatListResponse fromEntity(long total, List<ChatPartnerInfo> chatPartnerInfoList,
                                              CustomPage requestPage) {
        return ChatListResponse.builder()
                .total(total)
                .page(CustomPage.of(requestPage, chatPartnerInfoList.size()))
                .partnerList(chatPartnerInfoList)
                .build();
    }

}
