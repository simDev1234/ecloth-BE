package com.ecloth.beta.follow.repository;

import com.ecloth.beta.follow.entity.Follow;
import com.ecloth.beta.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByRequesterAndTarget(Member requester, Member target);

    Optional<Follow> findByRequesterAndTarget(Member requester, Member target);

}
