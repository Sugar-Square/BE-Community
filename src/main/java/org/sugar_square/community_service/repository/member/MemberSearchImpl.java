package org.sugar_square.community_service.repository.member;

import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.domain.member.QMember;
import org.sugar_square.community_service.enums.MemberOrderProps;

@RequiredArgsConstructor
public class MemberSearchImpl implements MemberSearch {

  private final JPAQueryFactory queryFactory;

  public Page<Member> searchAll(final String nickname, final Pageable pageable) {

    QMember member = QMember.member;

    // 여러 조건을 하나의 boolean builder 로 결합
    BooleanBuilder combinedBuilder = combineBuilders(getSearchBuilder(member, nickname));

    // 조건에 맞는 member 조회
    List<Member> result = queryFactory.selectFrom(member)
        .where(combinedBuilder)
        .orderBy(getOrderSpecifier(member, pageable.getSort()))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // member 카운트
    JPAQuery<Long> countQuery = queryFactory.from(member)
        .select(member.count())
        .where(combinedBuilder);

    return PageableExecutionUtils.getPage(result, pageable, () -> {
      Long count = countQuery.fetchOne();
      return Objects.isNull(count) ? 0L : count;
    });
  }

  private OrderSpecifier<?>[] getOrderSpecifier(QMember member, Sort sort) {
    Iterator<Order> orders = sort.iterator();
    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
    while (orders.hasNext()) {
      Order order = orders.next();
      MemberOrderProps prop = MemberOrderProps.fromString(order.getProperty()); // page 데이터 정렬 기준
      OrderSpecifier<?> orderSpecifier = switch (prop) {
        // 추후 정렬 조건 추가 가능
        case NICKNAME -> new OrderSpecifier<>(order.isAscending() ? ASC : DESC, member.nickname);
        case INVALID -> throw new IllegalArgumentException(
            "Invalid Member order property: " + order.getProperty()
        );
      };
      orderSpecifiers.add(orderSpecifier); // 정렬 기준에 따른 orderSpecifier 를 List 에 추가
    }
    return orderSpecifiers.toArray(OrderSpecifier[]::new);
  }

  private BooleanBuilder combineBuilders(final BooleanBuilder... builders) {
    BooleanBuilder builder = new BooleanBuilder();
    Arrays.stream(builders).forEach(builder::and);
    return builder;
  }

  // 현재는 nickname 만으로 검색 -> 추후 검색 조건을 wrapping 하는 DTO 를 만들어서 확장 가능
  private BooleanBuilder getSearchBuilder(final QMember member, final String nickname) {
    BooleanBuilder builder = new BooleanBuilder();
    if (!StringUtils.hasText(nickname)) {
      return builder;
    }
    builder.and(member.nickname.containsIgnoreCase(nickname));
    return builder;
  }
}
