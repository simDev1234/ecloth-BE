package com.ecloth.beta.domain.post.posting.repository;

import com.ecloth.beta.domain.post.posting.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
