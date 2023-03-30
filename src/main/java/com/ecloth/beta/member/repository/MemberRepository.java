package com.ecloth.beta.member.repository;

import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.model.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAuthCode(String emailAuthCode);

    Optional<Member> findByPasswordResetCode(String code);

    List<Member> findByMemberStatusAndUpdateDateBefore(MemberStatus status, LocalDateTime dateTime);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
