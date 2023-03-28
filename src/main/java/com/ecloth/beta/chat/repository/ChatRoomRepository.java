package com.ecloth.beta.chat.repository;

import com.ecloth.beta.chat.entity.ChatRoom;
import com.ecloth.beta.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Page<ChatRoom> findByMembersContaining(Member member, Pageable pageable);

}
