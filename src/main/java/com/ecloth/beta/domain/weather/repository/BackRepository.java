package com.ecloth.beta.domain.weather.repository;


import com.ecloth.beta.domain.weather.entity.BackImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackRepository extends JpaRepository<BackImg, Long> {

}

