package com.ecloth.beta.domain.location.repository;


import com.ecloth.beta.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationRepository extends JpaRepository<Member, Long> {


}
