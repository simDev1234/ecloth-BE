package com.ecloth.beta.domain.follow.repository;

import com.ecloth.beta.domain.follow.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FollowCustomRepository {

    Page<Follow> findFollowListByRequesterId(Long requesterId, Pageable pageable);
    Page<Follow> findFollowerListByTargetId(Long targetId, Pageable pageable);

}
