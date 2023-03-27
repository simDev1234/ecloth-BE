package com.ecloth.beta.config;

import com.ecloth.beta.member.entity.Member;
import com.ecloth.beta.member.model.MemberStatus;
import com.ecloth.beta.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {
    private final MemberRepository memberRepository;

    // 매주 월요일 자정 탈퇴 회원중 한달이상 업데이트 없는 회원 데이터 삭제
    @Scheduled(cron = "0 0 0 * * 1")
    public void deleteInactiveMembers(){
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Member> inactiveMembers = memberRepository.findByMemberStatusAndUpdateDateBefore(MemberStatus.INACTIVE,oneMonthAgo);
        memberRepository.deleteAll(inactiveMembers);
        log.info("Deleted " + inactiveMembers.size() + " inactive members.");
    }
}
