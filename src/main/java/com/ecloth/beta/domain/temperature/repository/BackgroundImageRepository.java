package com.ecloth.beta.domain.temperature.repository;

import com.ecloth.beta.domain.temperature.entity.BackgroundImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BackgroundImageRepository extends JpaRepository<BackgroundImage, Long> {
    List<BackgroundImage> findByBackgroundId(Long backgroundId);
}


