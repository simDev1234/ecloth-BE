package com.ecloth.beta.chat.service;

import com.ecloth.beta.chat.document.ChatMessage;
import com.ecloth.beta.chat.dto.ChatRoomListResponse;
import com.ecloth.beta.chat.dto.ChatRoomListResponse.ChatRoomInfo;
import com.ecloth.beta.chat.dto.ChatRoomCreateRequest;
import com.ecloth.beta.chat.dto.ChatRoomCreateResponse;
import com.ecloth.beta.chat.entity.ChatRoom;
import com.ecloth.beta.chat.exception.ChatException;
import com.ecloth.beta.chat.exception.ErrorCode;
import com.ecloth.beta.chat.repository.ChatRoomRepository;
import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 채팅(룸) 서비스
 */
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;

    // 채팅(룸) 생성
    public ChatRoomCreateResponse createChat(ChatRoomCreateRequest request) {

        Set<Member> chatRoomMembers = new HashSet<>();
        request.getMemberIds().forEach(x -> chatRoomMembers.add(memberRepository.findById(x)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"))
        ));

        ChatRoom newChatRoom = chatRoomRepository.save(ChatRoom.builder().members(chatRoomMembers).build());

        return ChatRoomCreateResponse.fromEntity(newChatRoom);
    }

    // 회원이 속한 채팅룸 목록 조회
    public ChatRoomListResponse findChatList(Long memberId, CustomPage requestPage) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        Page<ChatRoom> chatRooms = chatRoomRepository.findByMembersContaining(member, toPageable(requestPage));
        CustomPage pageResult = getPageResultInfo(requestPage, chatRooms);

        List<ChatRoomInfo> chatRoomInfoList = new ArrayList<>();
        for (ChatRoom room : chatRooms.getContent()) {
            Member partnerMember = room.getMembers().stream().filter(x -> x != member).findFirst()
                            .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));
            Optional<ChatMessage> latestPartnerMessage =
                    chatMessageService.findLatestPartnerMessage(room.getChatRoomId(), partnerMember.getMemberId());
            chatRoomInfoList.add(ChatRoomInfo.of(room, partnerMember, latestPartnerMessage));
        }

        return ChatRoomListResponse.fromEntity(chatRooms.getTotalElements(), pageResult, chatRoomInfoList);
    }

    private Pageable toPageable(CustomPage requestPage) {
        int page = requestPage.getPage() == 0 ? 1 : requestPage.getPage();
        int size = requestPage.getSize() == 0 ? 5 : requestPage.getSize();
        String sortBy = StringUtils.hasText(requestPage.getSortBy()) ? requestPage.getSortBy() : "registerDate";
        Sort.Direction dir = StringUtils.hasText(requestPage.getSortOrder()) ?
                Sort.Direction.valueOf(requestPage.getSortOrder().toUpperCase(Locale.ROOT)) : Sort.Direction.DESC;
        return PageRequest.of(page - 1, size, dir, sortBy);
    }

    private CustomPage getPageResultInfo(CustomPage requestPage, Page<ChatRoom> chatRooms) {
        return new CustomPage(
                chatRooms.getNumber(), chatRooms.getSize(), requestPage.getSortBy(), requestPage.getSortBy()
        );
    }

    // 채팅(룸) 삭제
    public void deleteChatRoom(Long chatRoomId) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                        .orElseThrow(() -> new ChatException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        chatRoomRepository.delete(chatRoom);
    }

}