package com.ecloth.beta.follow.repository;

import com.ecloth.beta.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("select f from Follow f where f.requester.email=:requesterEmail and f.target.memberId=:targetId")
    Optional<Follow> findByRequesterEmailAndTargetId(String requesterEmail, Long targetId);

    @Query("select f from Follow f join fetch f.target where f.requester.email = :email")
    List<Follow> findFollowListByRequesterEmail(String email);

    @Query("select f from Follow f join fetch f.target where f.requester.memberId = :memberId")
    List<Follow> findFollowListByRequesterId(Long memberId);

    @Query("select f from Follow f join fetch f.requester where f.target.email = :email")
    List<Follow> findFollowerListByTargetEmail(String email);

    @Query("select f from Follow f join fetch f.requester where f.target.memberId = :memberId")
    List<Follow> findFollowerListByTargetId(Long memberId);

}
