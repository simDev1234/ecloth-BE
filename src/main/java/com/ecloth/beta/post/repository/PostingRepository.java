package com.ecloth.beta.post.repository;

import com.ecloth.beta.post.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long> {
}


