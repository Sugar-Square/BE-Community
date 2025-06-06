package org.sugar_square.community_service.repository.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sugar_square.community_service.controller.board.PostController.SearchCondition;
import org.sugar_square.community_service.domain.board.Post;

@RequiredArgsConstructor
public class PostSearchImpl implements PostSearch {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Post> searchAll(
      final Long categoryId, final SearchCondition condition, final Pageable pageable
  ) {
    return null;
  }
}
