package com.ecloth.beta.domain.post.posting.repository;

import com.ecloth.beta.domain.member.entity.Member;
import com.ecloth.beta.domain.post.posting.entity.Posting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostingRepository extends JpaRepository<Posting, Long>, PostingCustomRepository {

    @Query("select p from Posting p join fetch p.imageList where p.postingId = :postingId")
    Optional<Posting> findByPostingIdFetchJoinedWithMember(Long postingId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Posting p WHERE p.postingId = :postingId AND p.writer.memberId = :memberId")
    boolean isPostingWriterAndReplyWriterMatching(Long postingId, Long memberId);

    Page<Posting> findByWriter(Member writer, Pageable pageable);

    boolean existsById(Long postingId);

}



