package com.ecloth.beta.domain.follow.repository;

import com.ecloth.beta.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowCustomRepository{

    @Query("select f from Follow f where f.requester.memberId=:requesterId and f.target.memberId=:targetId")
    Optional<Follow> findByRequesterIdAndTargetId(Long requesterId, Long targetId);

}
