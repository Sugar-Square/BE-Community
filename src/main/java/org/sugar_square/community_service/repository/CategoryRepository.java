package org.sugar_square.community_service.repository;

import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.board.Category;

@Repository
public interface CategoryRepository extends BaseRepository<Category, Long>{

}
