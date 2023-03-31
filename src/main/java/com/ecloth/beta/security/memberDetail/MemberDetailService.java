package com.ecloth.beta.security.memberDetail;

import com.ecloth.beta.domain.member.entity.Member;
import com.ecloth.beta.domain.member.exception.MemberErrorCode;
import com.ecloth.beta.domain.member.exception.MemberException;
import com.ecloth.beta.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public MemberDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.parseLong(id))
                .orElseThrow(()->new MemberException(MemberErrorCode.NOT_FOUND_USER));

        return new MemberDetails(member);
    }
}
