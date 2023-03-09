package com.ecloth.beta.follow.repository;

import com.ecloth.beta.follow.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByRequesterIdAndTargetId(Long requesterId, Long targetId);

    Page<Follow> findAllByTargetId(Long targetId, Pageable pageable);

}
