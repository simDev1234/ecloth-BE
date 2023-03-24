package com.ecloth.beta.domain.clothes.repository;

import com.ecloth.beta.domain.clothes.entity.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClothesRepository extends JpaRepository<Clothes, Long> {

    Optional<Clothes> findByImgPath(String imgPath);

    Optional<Clothes> findByStep(Long step);
}

