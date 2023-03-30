package com.ecloth.beta.domain.location.service;


import com.ecloth.beta.domain.location.dto.Locational;
import com.ecloth.beta.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecloth.beta.member.repository.MemberRepository;

@Service
public class LocationService {

    private final MemberRepository memberRepository;

    @Autowired
    public LocationService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void updateLocation(Long memberId, Locational locational) {
        Member member = Member.builder()
                .x(locational.getX())
                .y(locational.getY())
                .build();
        memberRepository.save(member);
    }
}


