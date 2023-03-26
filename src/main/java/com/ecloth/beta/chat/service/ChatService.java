package com.ecloth.beta.chat.service;

import com.ecloth.beta.chat.dto.ChatListResponse;
import com.ecloth.beta.chat.dto.ChatListResponse.ChatPartnerInfo;
import com.ecloth.beta.chat.entity.Chat;
import com.ecloth.beta.chat.repository.ChatRepository;
import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * 채팅(룸) 서비스
 */
@Service
@RequiredArgsConstructor
public class ChatService {

    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;

    // 채팅(룸) 생성
    public void createChat(String currentLoggedInMemberEmail, Long memberId) {

        Member currentLoggedInMember = memberRepository.findByEmail(currentLoggedInMemberEmail)
                .orElseThrow(() -> new RuntimeException("Member not Found"));

        Member chatPartner = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not Found"));

        Optional<Chat> optionalChat = chatRepository.findChatByMemberIds(
                currentLoggedInMember.getMemberId(), chatPartner.getMemberId());

        if (optionalChat.isEmpty()) {
            chatRepository.save(Chat.builder()
                    .firstMemberId(currentLoggedInMember.getMemberId())
                    .secondMemberId(chatPartner.getMemberId())
                    .build());
        }
    }

    // 로그인 회원이 속한 채팅(룸) 목록 조회
    public ChatListResponse findChatList(String currentLoggedInMemberEmail, CustomPage requestPage) {

        Member currentLoggedInMember = memberRepository.findByEmail(currentLoggedInMemberEmail)
                .orElseThrow(() -> new RuntimeException("Member not Found"));

        List<ChatPartnerInfo> chatPartnerInfoList = chatRepository.findChatListByMemberId(currentLoggedInMember.getMemberId());
        long total = chatPartnerInfoList.size();
        List<ChatPartnerInfo> subChatPartnerInfoList
                = chatPartnerInfoList.subList(requestPage.getStartIdx(), requestPage.getEndIdx(total));

        // TODO 몽고 db에서 최신 메세지 정보를 가져와 subChatListByPage에 추가

        return ChatListResponse.fromEntity(total, subChatPartnerInfoList, requestPage);
    }

    // 채팅(룸) 삭제
    public void deleteChat(String currentLoggedInMemberEmail, Long memberId) {

        Member currentLoggedInMember = memberRepository.findByEmail(currentLoggedInMemberEmail)
                .orElseThrow(() -> new RuntimeException("Member not Found"));

        chatRepository.findChatByMemberIds(currentLoggedInMember.getMemberId(), memberId)
                .ifPresent(chatRepository::delete);
    }

}