package com.ecloth.beta.follow.repository;

import com.ecloth.beta.common.page.CustomPage;
import com.ecloth.beta.follow.dto.FollowListResponse.MemberShortInfo;
import com.ecloth.beta.follow.dto.FollowingResponse.MemberFollowInfo;
import com.ecloth.beta.follow.entity.Follow;
import com.ecloth.beta.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByRequesterAndTarget(Member requester, Member target);

    Optional<Follow> findByRequesterAndTarget(Member requester, Member target);

    @Query("select f from Follow f where f.requester.email=:requesterEmail and f.target.memberId=:targetId")
    Optional<Follow> findByRequesterEmailAndTargetId(String requesterEmail, Long targetId);

//    @Query("select " +
//               "m.memberId as targetId," +
//               "m.nickname as nickname," +
//               "m.profileImagePath as profileImagePath " +
//            "from Follow f " +
//            "join Member m on f.requester.memberId = m.memberId where f.requester.email = :email " +
//            "order by :")
    List<MemberShortInfo> findFollowListByMemberEmail(String email, CustomPage page);

}
