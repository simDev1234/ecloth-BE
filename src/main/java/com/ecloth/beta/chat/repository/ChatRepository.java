package com.ecloth.beta.chat.repository;

import com.ecloth.beta.chat.dto.ChatListResponse.ChatPartnerInfo;
import com.ecloth.beta.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("select c from Chat c " +
            "where (c.firstMemberId = :memberId1 and c.secondMemberId = :memberId2) " +
            "or (c.firstMemberId = :memberId2 and c.secondMemberId = :memberId1)")
    Optional<Chat> findChatByMemberIds(Long memberId1, Long memberId2);

    @Query( value =
            "  select m.memberId, m.nickname, m.profileImagePath, c.registerDate  " +
            "  from member m  " +
            "  join chat c  " +
            "  on c.firstMemberId = m.memberId " +
            "  where c.firstMemberId = :memberId " +
            "union " +
            "  select m.memberId, m.nickname, m.profileImagePath, c.registerDate  " +
            "  from member m " +
            "  join chat c " +
            "  on c.secondMemberId = m.memberId " +
            "  where c.secondMemberId = :memberId " +
            "order by c.registerDate desc", nativeQuery = true)
    List<ChatPartnerInfo> findChatListByMemberId(Long memberId);

}
