package com.ecloth.beta.domain.post.posting.repository;

import com.ecloth.beta.domain.post.posting.entity.Posting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostingCustomRepository {

    Optional<Posting> findByPostingIdFetchJoinedWithMemberAndImage(Long postingId);

    Page<Posting> findPostingByPaging(Pageable pageable);

}



