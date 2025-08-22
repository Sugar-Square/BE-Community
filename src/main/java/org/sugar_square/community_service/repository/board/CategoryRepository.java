package org.sugar_square.community_service.repository.board;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.board.Category;
import org.sugar_square.community_service.repository.BaseRepository;

@Repository
public interface CategoryRepository extends BaseRepository<Category, Long> {

  // 공백에 제거된 카테고리 이름끼리 비교하여 중복된 이름이 있는지 확인. for PostgreSQL
  @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Category c WHERE REPLACE(c.name, ' ', '') = :name")
  boolean existsByNameWithoutSpaces(@Param("name") String name);
}
