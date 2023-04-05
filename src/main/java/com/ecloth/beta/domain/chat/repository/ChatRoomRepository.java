package com.ecloth.beta.domain.chat.repository;

import com.ecloth.beta.domain.chat.entity.ChatRoom;
import com.ecloth.beta.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Page<ChatRoom> findByMembersContaining(Member member, Pageable pageable);

    boolean existsByChatRoomIdAndMembersContaining(Long chatRoomId, Member member);

    boolean existsByMembersContainingAndMembersContaining(Member member1, Member member2);

}
