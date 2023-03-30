package com.ecloth.beta.follow.repository;

import com.ecloth.beta.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("select f from Follow f where f.requester.memberId=:requesterId and f.target.memberId=:targetId")
    Optional<Follow> findByRequesterIdAndTargetId(Long requesterId, Long targetId);

    @Query("select distinct f from Follow f join fetch f.target " +
            "where f.requester.memberId = :memberId order by f.registerDate desc")
    List<Follow> findFollowListByRequesterId(Long memberId);

    @Query("select distinct f from Follow f join fetch f.requester " +
            "where f.target.memberId = :memberId order by f.registerDate desc")
    List<Follow> findFollowerListByTargetId(Long memberId);

}
