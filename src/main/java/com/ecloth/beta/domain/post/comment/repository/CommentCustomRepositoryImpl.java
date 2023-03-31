package com.ecloth.beta.domain.post.comment.repository;

import com.ecloth.beta.domain.member.entity.QMember;
import com.ecloth.beta.domain.post.comment.entity.Comment;
import com.ecloth.beta.domain.post.comment.entity.QComment;
import com.ecloth.beta.domain.post.comment.entity.QReply;
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
public class CommentCustomRepositoryImpl implements CommentCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Comment> findByPostingIdJoinedWithMemberAndReply(Long postingId, Pageable pageable) {

        QComment comment = QComment.comment;
        QMember member = QMember.member;
        QReply reply = QReply.reply;

        List<Comment> commentList = jpaQueryFactory
                .selectFrom(comment)
                .join(comment.writer, member)
                .fetchJoin()
                .join(comment.reply, reply)
                .fetchJoin()
                .where(comment.posting.postingId.eq(postingId))
                .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getPageNumber() - 1)
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.posting.postingId.eq(postingId))
                .fetchOne();

        return new PageImpl<>(commentList, pageable, total);
    }

    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {

        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Comment.class, "comment");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(property)));
        });

        return orders;
    }


}
