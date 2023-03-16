package com.ecloth.beta.clothes.repository;


import com.ecloth.beta.clothes.entity.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClothesRepository extends JpaRepository<Clothes, Long> {

    Optional<Clothes> findByImgPath(String ImgPath);

    Optional<Clothes> findByStep(int step);
}
