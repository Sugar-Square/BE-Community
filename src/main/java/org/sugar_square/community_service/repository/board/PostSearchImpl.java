package org.sugar_square.community_service.repository.board;

import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;
import static org.sugar_square.community_service.enums.PostSearchType.INVALID;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
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
import org.sugar_square.community_service.controller.board.PostController.SearchCondition;
import org.sugar_square.community_service.domain.board.Category;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.board.QPost;
import org.sugar_square.community_service.enums.PostSearchType;

@RequiredArgsConstructor
public class PostSearchImpl implements PostSearch {

  private final JPAQueryFactory queryFactory;

  /*
   * PageableExecution.getPage() 로 카운트 쿼리 지연 :
   * getPage 메서드는 현재 페이지의 데이터가 페이지 크기보다 작으면 카운트 쿼리를 실행하지 않는다.
   * (이전 페이지 개수 * 페이지 최대 크기 + 현재 페이지 데이터 개수) 로 total 을 계산한다.
   * 따라서 카운트 쿼리가 지연되고 성능 향상 효과가 있다.
   * 이를 위해 카운트 쿼리를 미리 실행하지 않고, 쿼리를 람다 함수로써 매개변수로 넣어 줌.
   */
  @Override
  public Page<Post> searchAll(
      final Category category, final SearchCondition condition, final Pageable pageable
  ) {

    QPost post = QPost.post;
    BooleanBuilder combinedBuilder = combineBuilders(
        getCategoryBuilder(post, category),
        getDateBuilder(post, condition.getStartDate(), condition.getEndDate()),
        getSearchBuilder(post, condition.getSearchType(), condition.getKeyword())
    );

    // page content
    List<Post> result = queryFactory.selectFrom(post)
        .where(combinedBuilder)
        .orderBy(getOrderSpecifier(post, pageable.getSort()))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // total count query
    JPAQuery<Long> countQuery = queryFactory.from(post)
        .select(post.count())
        .where(combinedBuilder);

    // return page
    return PageableExecutionUtils.getPage(result, pageable, () -> {
      Long count = countQuery.fetchOne();
      return Objects.isNull(count) ? 0L : count;
    });
  }

  private OrderSpecifier<?>[] getOrderSpecifier(QPost post, Sort sort) {
    Iterator<Order> orders = sort.iterator();
    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
    while (orders.hasNext()) {
      Order order = orders.next();
      OrderSpecifier<?> orderspecifier = switch (order.getProperty()) {
        case "createdAt" -> new OrderSpecifier<>(order.isAscending() ? ASC : DESC, post.createdAt);
        case "title" -> new OrderSpecifier<>(order.isAscending() ? ASC : DESC, post.title);
        case "writer" ->
            new OrderSpecifier<>(order.isAscending() ? ASC : DESC, post.writer.nickname);
        case "id" -> new OrderSpecifier<>(order.isAscending() ? ASC : DESC, post.id);
        default -> throw new IllegalArgumentException(
            "Unexpected Post order property: " + order.getProperty()
        );
      };
      orderSpecifiers.add(orderspecifier);
    }
    return orderSpecifiers.toArray(OrderSpecifier[]::new);
  }

  private BooleanBuilder combineBuilders(BooleanBuilder... builders) {
    BooleanBuilder builder = new BooleanBuilder();
    Arrays.stream(builders).forEach(builder::and);
    return builder;
  }

  private BooleanBuilder getCategoryBuilder(QPost post, Category category) {
    BooleanBuilder builder = new BooleanBuilder();
    return builder.and(post.category.eq(category));
  }

  private BooleanBuilder getDateBuilder(QPost post, Instant start, Instant end) {
    BooleanBuilder builder = new BooleanBuilder();
    boolean hasStartDate = start != null;
    boolean hasEndDate = end != null;
    if (hasStartDate && hasEndDate) {
      builder.and(post.createdAt.between(start, end));
    }
    return builder;
  }

  private BooleanBuilder getSearchBuilder(QPost post, PostSearchType type, String keyword) {
    BooleanBuilder builder = new BooleanBuilder();
    boolean hasType = type != INVALID;
    boolean hasKeyword = StringUtils.hasText(keyword);
    if (hasType && hasKeyword) {
      switch (type) {
        case TITLE -> builder.and(post.title.containsIgnoreCase(keyword));
        case CONTENT -> builder.and(post.content.containsIgnoreCase(keyword));
        case TITLE_AND_CONTENT -> builder.or(post.title.containsIgnoreCase(keyword)
            .or(post.content.containsIgnoreCase(keyword)));
        case WRITER -> builder.and(post.writer.nickname.containsIgnoreCase(keyword));
      }
    }
    return builder;
  }
}
