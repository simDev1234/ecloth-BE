package com.ecloth.beta.domain.post.posting.repository;

import com.ecloth.beta.domain.member.entity.QMember;
import com.ecloth.beta.domain.post.posting.entity.Posting;
import com.ecloth.beta.domain.post.posting.entity.QImage;
import com.ecloth.beta.domain.post.posting.entity.QPosting;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostingCustomRepositoryImpl implements PostingCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Posting> findByPostingIdFetchJoinedWithMemberAndImage(Long postingId) {

        QPosting posting = QPosting.posting;
        QMember member = QMember.member;
        QImage image = QImage.image;

        Posting foundPosting = jpaQueryFactory
                .selectFrom(posting)
                .join(posting.writer, member)
                .fetchJoin()
                .leftJoin(posting.imageList, image)
                .fetchJoin()
                .where(posting.postingId.eq(postingId))
                .fetchOne();

        return Optional.ofNullable(foundPosting);
    }

    @Override
    public Page<Posting> findPostingByPaging(Pageable pageable) {

        QPosting posting = QPosting.posting;
        QImage image = QImage.image;

        List<Posting> postingList = jpaQueryFactory
                .selectFrom(posting)
                .leftJoin(posting.imageList, image)
                .fetchJoin()
                .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getPageNumber() - 1)
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(posting.count())
                .from(posting)
                .fetchOne();

        return new PageImpl<>(postingList, pageable, total);
    }

    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {

        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Posting.class, "posting");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(property)));
        });

        return orders;
    }

}



