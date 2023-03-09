package com.ecloth.beta.member.repository;

import com.ecloth.beta.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAuthCode(String emailAuthCode);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
