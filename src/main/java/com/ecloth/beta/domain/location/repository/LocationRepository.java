package com.ecloth.beta.domain.location.repository;

import com.ecloth.beta.domain.location.entity.Locational;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationRepository extends JpaRepository<Locational, Long> {


}
