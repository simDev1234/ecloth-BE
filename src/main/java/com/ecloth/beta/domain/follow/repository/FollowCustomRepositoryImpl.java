package com.ecloth.beta.domain.follow.repository;

import com.ecloth.beta.domain.follow.entity.Follow;
import com.ecloth.beta.domain.follow.entity.QFollow;
import com.ecloth.beta.domain.member.entity.QMember;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowCustomRepositoryImpl implements FollowCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Follow> findFollowListByRequesterId(Long requesterId, Pageable pageable) {

        QFollow follow = QFollow.follow;
        QMember member = QMember.member;

        List<Follow> followList = jpaQueryFactory
                .selectFrom(follow)
                .join(follow.target, member)
                .fetchJoin()
                .where(follow.requester.memberId.eq(requesterId))
                .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getPageNumber() - 1)
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(member.followList.size())
                .from(member)
                .where(member.memberId.eq(requesterId))
                .fetchOne();

        return new PageImpl<>(followList, pageable, total);
    }

    @Override
    public Page<Follow> findFollowerListByTargetId(Long targetId, Pageable pageable) {

        QFollow follow = QFollow.follow;
        QMember member = QMember.member;

        List<Follow> followerList = jpaQueryFactory
                .selectFrom(follow)
                .join(follow.requester, member)
                .fetchJoin()
                .where(follow.target.memberId.eq(targetId))
                .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getPageNumber() - 1)
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(member.followerList.size())
                .from(member)
                .where(member.memberId.eq(targetId))
                .fetchOne();

        return new PageImpl<>(followerList, pageable, total);
    }

    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {

        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Follow.class, "follow");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(property)));
        });

        return orders;
    }

}
